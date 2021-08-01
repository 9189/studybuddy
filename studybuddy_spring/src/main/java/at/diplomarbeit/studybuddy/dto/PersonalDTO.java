package at.diplomarbeit.studybuddy.dto;

import at.diplomarbeit.studybuddy.data.entity.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class PersonalDTO extends UserDTO {
    private final String token;
    private final Set<Inserat> gemerkteInserate;
    private final Set<UserDTO> contacts = new HashSet<>();

    public PersonalDTO(Long id,
                       String name,
                       String email,
                       String beschreibung,
                       LocalDate gebDat,
                       Stadt herkunftsStadt,
                       Studienrichtung studienrichtung,
                       Bild profilbild,
                       Inserat gruppe,
                       Set<Sprache> sprachen,
                       Set<Inserat> gemerkteInserate,
                       Set<User> contacts,
                       String token) {
        super(id, name, email, beschreibung, gebDat, herkunftsStadt, studienrichtung, profilbild, gruppe, sprachen);

        this.gemerkteInserate = gemerkteInserate;
        this.token = token;

        for(User user : contacts) {
            this.contacts.add(new UserDTO(
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
            ));
        }
    }

    public Set<Inserat> getGemerkteInserate() {
        return gemerkteInserate;
    }

    public Set<UserDTO> getContacts() {
        return contacts;
    }

    public String getToken() {
        return token;
    }

}
