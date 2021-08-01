package at.diplomarbeit.studybuddy.util;

import at.diplomarbeit.studybuddy.data.entity.User;
import at.diplomarbeit.studybuddy.data.repository.UserRepository;
import at.diplomarbeit.studybuddy.dto.PersonalDTO;
import at.diplomarbeit.studybuddy.dto.UserDTO;
import at.diplomarbeit.studybuddy.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtility {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserUtility(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    public PersonalDTO getPersonalFromUser(User user, String token) {
        return new PersonalDTO(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getBeschreibung(),
                user.getGebDat(),
                user.getHerkunftsStadt(),
                user.getStudienrichtung(),
                user.getProfilbild(),
                user.getInseratGruppe(),
                user.getSprachen(),
                user.getGemerkteInserate(),
                user.getContacts(),
                token
        );
    }

    public UserDTO getPublicFromUser(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getBeschreibung(),
                user.getGebDat(),
                user.getHerkunftsStadt(),
                user.getStudienrichtung(),
                user.getProfilbild(),
                user.getInseratGruppe(),
                user.getSprachen()
        );
    }

    public User getUserFromToken(String token) {
        return userRepository.findUserByEmail(jwtProvider.getEmail(token)).orElseThrow();
    }

    public String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    public User getUserFromHeader(String header) {
        return getUserFromToken(getTokenFromHeader(header));
    }
}