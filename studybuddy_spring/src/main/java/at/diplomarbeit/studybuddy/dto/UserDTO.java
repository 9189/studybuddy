package at.diplomarbeit.studybuddy.dto;

import at.diplomarbeit.studybuddy.data.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String beschreibung;
    @JsonFormat(pattern="dd.MM.yyyy") private LocalDate gebDat;
    private int alter;
    private Stadt herkunftsStadt;
    private Land herkunftsLand;
    private Studienrichtung studienrichtung;
    private Bild profilbild;
    private Inserat gruppe;
    private Set<Sprache> sprachen;

    public UserDTO(Long id,
                   String name,
                   String email,
                   String beschreibung,
                   LocalDate gebDat,
                   Stadt herkunftsStadt,
                   Studienrichtung studienrichtung,
                   Bild profilbild,
                   Inserat gruppe,
                   Set<Sprache> sprachen) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.beschreibung = beschreibung;
        this.gebDat = gebDat;
        this.herkunftsStadt = herkunftsStadt;
        this.herkunftsLand = herkunftsStadt.getLand();
        this.studienrichtung = studienrichtung;
        this.profilbild = profilbild;
        this.gruppe = gruppe;
        this.sprachen = sprachen;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public LocalDate getGebDat() {
        return gebDat;
    }

    public int getAlter() {
        return Period.between(gebDat, LocalDate.now()).getYears();
    }

    public Stadt getHerkunftsStadt() {
        return herkunftsStadt;
    }

    public Land getHerkunftsLand() {
        return herkunftsLand;
    }

    public Studienrichtung getStudienrichtung() {
        return studienrichtung;
    }

    public Bild getProfilbild() {
        return profilbild;
    }

    public Inserat getGruppe() {
        return gruppe;
    }

    public Set<Sprache> getSprachen() {
        return sprachen;
    }
}
