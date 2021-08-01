package at.diplomarbeit.studybuddy.dto;

import java.time.LocalDate;
import java.util.Date;

public class RegistrierungsDTO {
    private String name;
    private String email;
    private String passwort;
    private String beschreibung;
    private LocalDate gebDat;
    private Long herkunftsStadtId;
    private Long studienrichtungId;
    private String[] sprachenIds;

    public RegistrierungsDTO(String name,
                             String email,
                             String passwort,
                             String beschreibung,
                             LocalDate gebDat,
                             Long herkunftsStadtId,
                             Long studienrichtungId,
                             String[] sprachenIds) {
        this.name = name;
        this.email = email;
        this.passwort = passwort;
        this.beschreibung = beschreibung;
        this.gebDat = gebDat;
        this.herkunftsStadtId = herkunftsStadtId;
        this.studienrichtungId = studienrichtungId;
        this.sprachenIds = sprachenIds;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswort() {
        return passwort;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public LocalDate getGebDat() {
        return gebDat;
    }

    public Long getHerkunftsStadtId() {
        return herkunftsStadtId;
    }

    public Long getStudienrichtungId() {
        return studienrichtungId;
    }

    public String[] getSprachenIds() {
        return sprachenIds;
    }
}
