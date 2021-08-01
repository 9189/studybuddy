package at.diplomarbeit.studybuddy.dto;

import com.sun.istack.NotNull;

public class LoginDTO {
    @NotNull
    private String email;

    @NotNull
    private String passwort;

    public LoginDTO(String email, String passwort) {
        this.email = email;
        this.passwort = passwort;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswort() {
        return passwort;
    }
}
