package at.diplomarbeit.studybuddy.service;

import at.diplomarbeit.studybuddy.data.entity.*;
import at.diplomarbeit.studybuddy.data.repository.*;
import at.diplomarbeit.studybuddy.dto.RegistrierungsOptionsDTO;
import at.diplomarbeit.studybuddy.dto.PersonalDTO;
import at.diplomarbeit.studybuddy.dto.UserDTO;
import at.diplomarbeit.studybuddy.security.JwtProvider;
import at.diplomarbeit.studybuddy.util.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SpracheRepository spracheRepository;
    private final LandRepository landRepository;
    private final StudienrichtungRepository studienrichtungRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StadtRepository stadtRepository;
    private final InseratRepository inseratRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtProvider refreshtokenProvider;
    private final UserUtility userUtility;

    @Autowired
    public UserService(UserRepository userRepository,
                       SpracheRepository spracheRepository,
                       LandRepository landRepository,
                       StudienrichtungRepository studienrichtungRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       StadtRepository stadtRepository,
                       InseratRepository inseratRepository,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       JwtProvider jwtProvider,
                       JwtProvider refreshtokenProvider,
                       UserUtility UserUtility) {
        this.userRepository = userRepository;
        this.spracheRepository = spracheRepository;
        this.landRepository = landRepository;
        this.studienrichtungRepository = studienrichtungRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.stadtRepository = stadtRepository;
        this.inseratRepository = inseratRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.refreshtokenProvider = refreshtokenProvider;
        this.userUtility = UserUtility;
    }

    public Optional<PersonalDTO> login(String email, String passwort, HttpServletResponse response) {
        Optional<PersonalDTO> userDTO = Optional.empty();
        Optional<User> user = userRepository.findUserByEmail(email);

        if (user.isPresent()) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, passwort));
                Optional<String> token = Optional.of(jwtProvider.createToken(email));

                response.addHeader("Set-Cookie", createRefreshtokenCookie(user.orElseThrow()).toString());

                User existingUser = user.get();

                userDTO = Optional.of(
                        new PersonalDTO(
                                existingUser.getUserId(),
                                existingUser.getName(),
                                existingUser.getEmail(),
                                existingUser.getBeschreibung(),
                                existingUser.getGebDat(),
                                existingUser.getHerkunftsStadt(),
                                existingUser.getStudienrichtung(),
                                existingUser.getProfilbild(),
                                existingUser.getInseratGruppe(),
                                existingUser.getSprachen(),
                                existingUser.getGemerkteInserate(),
                                existingUser.getContacts(),
                                token.orElseThrow()
                                )
                );

            } catch (AuthenticationException e){
                System.out.println(e.getMessage());
            }
        }

        return userDTO;
    }

    public Optional<PersonalDTO> refresh(HttpServletRequest request, HttpServletResponse response) {
        Optional<PersonalDTO> userDTO = Optional.empty();

        try {
            Cookie[] cookies = request.getCookies();

            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("refresh")) {
                    if(refreshTokenRepository.existsById(cookie.getValue())) {
                        String email = jwtProvider.getEmail(cookie.getValue());

                        Optional<String> token = Optional.of(jwtProvider.createToken(email));
                        Optional<User> user = userRepository.findUserByEmail(email);

                        if(user.isPresent()) {
                            User existingUser = user.get();

                            userDTO = Optional.of(
                                    new PersonalDTO(
                                            existingUser.getUserId(),
                                            existingUser.getName(),
                                            existingUser.getEmail(),
                                            existingUser.getBeschreibung(),
                                            existingUser.getGebDat(),
                                            existingUser.getHerkunftsStadt(),
                                            existingUser.getStudienrichtung(),
                                            existingUser.getProfilbild(),
                                            existingUser.getInseratGruppe(),
                                            existingUser.getSprachen(),
                                            existingUser.getGemerkteInserate(),
                                            existingUser.getContacts(),
                                            token.orElseThrow()
                                    )
                            );

                            response.addHeader("Set-Cookie", createRefreshtokenCookie(user.orElseThrow()).toString());
                        }
                        break;
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        return userDTO;
    }

    public ResponseCookie createRefreshtokenCookie(User user) {
        refreshTokenRepository.deleteByUser(userRepository.findUserByEmail(user.getEmail()).orElseThrow());
        // Überschreiben der default JWT validity von 15 min auf 7 tage
        refreshtokenProvider.setValidityInMilliseconds(7 * 24 * 60 * 60 * 1000);
        String refreshToken = refreshtokenProvider.createToken(user.getEmail());
        refreshTokenRepository.save(new RefreshToken(refreshToken, user));

        Cookie cookie = new Cookie("refresh", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");
        cookie.setSecure(true);

        return ResponseCookie.from("refresh", refreshToken)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();
    }

    public boolean registrieren(String name,
                                String email,
                                String passwort,
                                String beschreibung,
                                LocalDate gebDat,
                                Long herkunftsStadtId,
                                Long studienrichtungId,
                                String[] sprachenIds) {
        if(userRepository.findUserByEmail(email).isEmpty()) {
            try {
                Stadt herkunftsStadt = stadtRepository.findById(herkunftsStadtId).orElseThrow();
                Studienrichtung studienrichtung = studienrichtungRepository.findById(studienrichtungId).orElseThrow();
                Set<Sprache> sprachen = new HashSet<>();

                for (String sprachenId : sprachenIds) {
                    sprachen.add(spracheRepository.findById(sprachenId).orElseThrow());
                }

                userRepository.save(
                        new User(name,
                                email,
                                passwordEncoder.encode(passwort),
                                beschreibung,
                                gebDat,
                                herkunftsStadt,
                                studienrichtung,
                                sprachen)
                );
                return true;
            } catch (NoSuchElementException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return false;
    }

    public RegistrierungsOptionsDTO getRegistrierungsOptions() {
        return new RegistrierungsOptionsDTO(
                landRepository.findAll(),
                spracheRepository.findAll(),
                studienrichtungRepository.findAll());
    }

    public Optional<UserDTO> getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        Optional<UserDTO> userDTO = Optional.empty();


        if (user.isPresent()) {
            User existingUser = user.get();

            userDTO = Optional.of(
                    new UserDTO(
                            existingUser.getUserId(),
                            existingUser.getName(),
                            existingUser.getEmail(),
                            existingUser.getBeschreibung(),
                            existingUser.getGebDat(),
                            existingUser.getHerkunftsStadt(),
                            existingUser.getStudienrichtung(),
                            existingUser.getProfilbild(),
                            existingUser.getInseratGruppe(),
                            existingUser.getSprachen()
                            )
            );
        }

        return userDTO;
    }

    public PersonalDTO addContact(HttpServletRequest request, Long userId) {
        String header = request.getHeader("Authorization");
        String token = userUtility.getTokenFromHeader(header);

        User user = userUtility.getUserFromToken(token);
        User user2 = userRepository.findById(userId).orElseThrow();

        user.addContact(user2);
        user2.addContact(user);
        userRepository.save(user);
        userRepository.save(user2);

        return userUtility.getPersonalFromUser(user, token);
    }

    public PersonalDTO getMe(HttpServletRequest request) {
        String token = userUtility.getTokenFromHeader(request.getHeader("Authorization"));
        User user = userUtility.getUserFromToken(token);

        return userUtility.getPersonalFromUser(user, token);
    }

    public boolean userRequestedMyInserat(HttpServletRequest request, Long userId) {
        String token = userUtility.getTokenFromHeader(request.getHeader("Authorization"));
        User user = userUtility.getUserFromToken(token);

        Optional<Inserat> optionalInserat = inseratRepository.findByErsteller(user);

        if(optionalInserat.isPresent()) {
            Inserat myInserat = optionalInserat.get();

            for(User anfragendeUser : myInserat.getInseratAnfragen()) {
                if(anfragendeUser.getUserId().equals(userId)) return true;
            }
        }

        return false;
    }

    public Iterable<Inserat> getGemerkteInserate(HttpServletRequest request) {
        User user = userUtility.getUserFromHeader(request.getHeader("Authorization"));

        return user.getGemerkteInserate();
    }

    public boolean emailInUse(String email) {
        return userRepository.existsByEmail(email);
    }
}
