package at.diplomarbeit.studybuddy.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

@Entity
@Table(name="user")
public class User {
    @Id
    @Column(name = "uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    private String email;

    @JsonIgnore
    private String passwort;

    private String beschreibung;

    @Column(name = "geb_dat")
    private LocalDate gebDat;

    @ManyToOne
    @JoinColumn(name = "herkunfts_stadt_id")
    private Stadt herkunftsStadt;

    @ManyToOne
    @JoinColumn(name = "studienrichtung_id")
    private Studienrichtung studienrichtung;

    @OneToOne
    @JoinColumn(name = "profilbild_id")
    private Bild profilbild;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "mitglied_ins_id")
    private Inserat inseratGruppe;

    @ManyToMany
    @JoinTable(name = "user_spricht_sprache",
            joinColumns = {@JoinColumn(name = "uid")},
            inverseJoinColumns = {@JoinColumn(name = "iso_code")})
    private Set<Sprache> sprachen;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_merkt_inserat",
            joinColumns = {@JoinColumn(name = "uid")},
            inverseJoinColumns = {@JoinColumn(name = "ins_id")})
    private Set<Inserat> gemerkteInserate;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "contacts",
        joinColumns = {@JoinColumn(name = "uid")},
        inverseJoinColumns = {@JoinColumn(name = "co_id")})
    private Set<User> contacts;

    protected User() {
    }

    public User(String name,
                String email,
                String passwort,
                String beschreibung,
                LocalDate gebDat,
                Stadt herkunftsStadt,
                Studienrichtung studienrichtung,
                Set<Sprache> sprachen
    ) {
        this.name = name;
        this.email = email;
        this.passwort = passwort;
        this.beschreibung = beschreibung;
        this.gebDat = gebDat;
        this.herkunftsStadt = herkunftsStadt;
        this.studienrichtung = studienrichtung;
        this.sprachen = sprachen;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public LocalDate getGebDat() {
        return gebDat;
    }

    public void setGebDat(LocalDate gebDat) {
        this.gebDat = gebDat;
    }

    public Stadt getHerkunftsStadt() {
        return herkunftsStadt;
    }

    public void setHerkunftsStadt(Stadt herkunftsStadt) {
        this.herkunftsStadt = herkunftsStadt;
    }

    public Studienrichtung getStudienrichtung() {
        return studienrichtung;
    }

    public void setStudienrichtung(Studienrichtung studienrichtung) {
        this.studienrichtung = studienrichtung;
    }

    public Bild getProfilbild() {
        return profilbild;
    }

    public void setProfilbild(Bild profilbild) {
        this.profilbild = profilbild;
    }

    public Inserat getInseratGruppe() {
        return inseratGruppe;
    }

    public void setInseratGruppe(Inserat gruppe) {
        this.inseratGruppe = gruppe;
    }

    public Set<Sprache> getSprachen() {
        return sprachen;
    }

    public void setSprachen(Set<Sprache> sprachen) {
        this.sprachen = sprachen;
    }

    public Set<Inserat> getGemerkteInserate() {
        return gemerkteInserate;
    }

    public void addContact(User contact) {
        contacts.add(contact);
    }

    public Set<User> getContacts() {
        return contacts;
    }

    public void inseratMerken(Inserat inserat) {
        gemerkteInserate.add(inserat);
    }

    public void inseratEntfernen(Long inseratId) {
        gemerkteInserate.removeIf(inserat -> inserat.getInseratId().equals(inseratId));
    }

    public int getAlter() {
        return Period.between(gebDat, LocalDate.now()).getYears();
    }
}