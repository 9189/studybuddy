package at.diplomarbeit.studybuddy.service;

import at.diplomarbeit.studybuddy.data.entity.Inserat;
import at.diplomarbeit.studybuddy.data.entity.User;
import at.diplomarbeit.studybuddy.data.repository.InseratRepository;
import at.diplomarbeit.studybuddy.data.repository.StadtRepository;
import at.diplomarbeit.studybuddy.data.repository.UserRepository;
import at.diplomarbeit.studybuddy.dto.ChatMessageDTO;
import at.diplomarbeit.studybuddy.dto.InseratInputDTO;
import at.diplomarbeit.studybuddy.util.InseratExpressions;
import at.diplomarbeit.studybuddy.util.UserUtility;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class InseratService {
    private final InseratRepository inseratRepository;
    private final StadtRepository stadtRepository;
    private final UserRepository userRepository;
    private final UserUtility userUtility;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public InseratService(InseratRepository inseratRepository,
                          StadtRepository stadtRepository,
                          UserRepository userRepository,
                          UserUtility userUtility,
                          SimpMessagingTemplate simpMessagingTemplate) {
        this.inseratRepository = inseratRepository;
        this.stadtRepository = stadtRepository;
        this.userRepository = userRepository;
        this.userUtility = userUtility;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void addInserat(InseratInputDTO inseratDTO, HttpServletRequest request) {
        User user = userUtility.getUserFromHeader(request.getHeader("Authorization"));

        if(inseratRepository.existsByErsteller(user)) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Bereits ein Inserate erstellt.");
        } else {
            inseratRepository.save(new Inserat(
                    true,
                    stadtRepository.findById(inseratDTO.getZielStadtId()).orElseThrow(),
                    inseratDTO.getUni(),
                    inseratDTO.getMaxUser(),
                    inseratDTO.getBeschreibung(),
                    inseratDTO.getVonDat(),
                    inseratDTO.getBisDat(),
                    user
            ));
        }
    }

    public void inseratAnfrage(HttpServletRequest request, Long inseratId) {
        String token = userUtility.getTokenFromHeader(request.getHeader("Authorization"));
        User user = userUtility.getUserFromToken(token);

        Inserat inserat = inseratRepository.findById(inseratId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.FORBIDDEN));
        Long inseratErstellerId = inserat.getErsteller().getUserId();


        inserat.anfragen(user);
        inseratRepository.save(inserat);

        User ersteller = inserat.getErsteller();
        ersteller.addContact(user);
        user.addContact(ersteller);
        userRepository.save(user);
        userRepository.save(ersteller);

        simpMessagingTemplate.convertAndSend(
                "/topic/user/contact-refresh/" + inseratErstellerId,
                "call /me now"
        );

        String message = "Hey " +
                inserat.getErsteller().getName() +
                ",\nich würde mich gerne deiner Gruppe anschließen!";

        simpMessagingTemplate.convertAndSend(
                "/topic/user/messages/" + inseratErstellerId,
                new ChatMessageDTO(
                        0L,
                        new HashSet<>(Arrays.asList(inseratErstellerId)),
                        message));
    }

    public void inseratMerken(Long inseratId, HttpServletRequest request) {
        User user = userUtility.getUserFromHeader(request.getHeader("Authorization"));
        user.inseratMerken(inseratRepository.findById(inseratId).orElseThrow());
        userRepository.save(user);
    }

    public void inseratEntmerken(Long inseratId, HttpServletRequest request) {
        User user = userUtility.getUserFromHeader(request.getHeader("Authorization"));
        user.inseratEntfernen(inseratId);
        userRepository.save(user);
    }

    public Inserat replaceInserat(InseratInputDTO newInserat, Long id) {
        return inseratRepository.findById(id).map(inserat -> {
            inserat.setIstAktiv(true);
            inserat.setZielStadt(stadtRepository.findById(newInserat.getZielStadtId()).orElseThrow());
            inserat.setUni(newInserat.getUni());
            inserat.setMaxUser(newInserat.getMaxUser());
            inserat.setBeschreibung(newInserat.getBeschreibung());
            inserat.setVonDat(inserat.getVonDat());
            inserat.setBisDat(inserat.getBisDat());

            return inseratRepository.save(inserat);
        }).orElseThrow();
    }

    public void addToGruppe(HttpServletRequest request, Long uid) {
        String token = userUtility.getTokenFromHeader(request.getHeader("Authorization"));
        User ersteller = userUtility.getUserFromToken(token);
        User user = userRepository.findById(uid).orElseThrow();

        Inserat inserat = inseratRepository.findByErsteller(ersteller).orElseThrow();

        if(inserat.getMaxUser() >= inserat.getMitglieder().size()) {
            user.setInseratGruppe(inserat);
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Maximale Anzahl an Mitgliedern bereits erreicht");
        }
    }

    public void removeFromGruppe(HttpServletRequest request, Long uid) {
        String token = userUtility.getTokenFromHeader(request.getHeader("Authorization"));
        User user = userUtility.getUserFromToken(token);

        Inserat inserat = inseratRepository.findByErsteller(user).orElseThrow();

        inserat.mitgliedEntfernen(uid);
        inseratRepository.save(inserat);
    }

    public Iterable<Inserat> search(Long landId, Long stadtId, Date vonDat, Date bisDat, Pageable pageable) {
        BooleanBuilder where = new BooleanBuilder();

        if(landId != null && stadtId != null) {
            where.and(InseratExpressions.hasZielStadt(stadtId));
        } else if(landId != null) {
            where.and(InseratExpressions.hasZielLand(landId));
        } else if(stadtId != null) {
            where.and(InseratExpressions.hasZielLand(stadtId));
        }

        if(vonDat != null) where.and(InseratExpressions.hasFromDate(vonDat));
        if(bisDat != null) where.and(InseratExpressions.hasToDate(bisDat));

        return inseratRepository.findAll(where, pageable);
    }

    public Optional<Inserat> getMine(HttpServletRequest request) {
        String token = userUtility.getTokenFromHeader(request.getHeader("Authorization"));
        User user = userUtility.getUserFromToken(token);

        return inseratRepository.findByErsteller(user);
    }

    public Boolean hasRequestedToJoinMine(HttpServletRequest request, Long uid) {
        Optional<Inserat> inserat = getMine(request);
        User user = userRepository.findById(uid).orElseThrow();
        boolean hasRequseted = false;

        if(inserat.isPresent()) {
            if(!inserat.get().getMitglieder().contains(user)) {
                hasRequseted = inserat.get().getInseratAnfragen().contains(user);
            }
        }

        return hasRequseted;
    }

}
