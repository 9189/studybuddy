package at.diplomarbeit.studybuddy.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="inserat")
public class Inserat {
    @Id
    @Column(name="ins_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inseratId;

    @Column(name="ist_aktiv")
    private boolean istAktiv;

    @JoinColumn(name="ziel_stadt_id")
    @ManyToOne
    private Stadt zielStadt;

    private String uni;

    @Column(name="max_user")
    private int maxUser;

    private String beschreibung;

    @Column(name="von_dat")
    @JsonFormat(pattern="dd.MM.yyyy")
    private Date vonDat;

    @Column(name="bis_dat")
    @JsonFormat(pattern="dd.MM.yyyy")
    private Date bisDat;

    @JoinColumn(name="ersteller_id", referencedColumnName = "uid")
    @OneToOne
    private User ersteller;

    @OneToMany(mappedBy = "inseratGruppe")
    private Set<User> mitglieder;

    @ManyToMany(mappedBy="gemerkteInserate")
    @JsonIgnore
    private Set<User> gemerktVonUsern;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "user_anfrage_inserat",
            joinColumns = {@JoinColumn(name = "ins_id")},
            inverseJoinColumns = {@JoinColumn(name = "uid")})
    private Set<User> inseratAnfragen;

    public Inserat() {

    }

    public Inserat(boolean istAktiv, Stadt zielStadt, String uni, int maxUser, String beschreibung, Date vonDat, Date bisDat, User ersteller) {
        this.istAktiv = istAktiv;
        this.zielStadt = zielStadt;
        this.uni = uni;
        this.maxUser = maxUser;
        this.beschreibung = beschreibung;
        this.vonDat = vonDat;
        this.bisDat = bisDat;
        this.ersteller = ersteller;
    }

    public Long getInseratId() {
        return inseratId;
    }

    public User getErsteller() {
        return ersteller;
    }

    public Set<User> getGemerktVonUsern() {
        return gemerktVonUsern;
    }

    public boolean getIstAktiv() {
        return istAktiv;
    }

    public void setIstAktiv(boolean istAktiv) {
        this.istAktiv = istAktiv;
    }

    public Stadt getZielStadt() {
        return zielStadt;
    }

    public void setZielStadt(Stadt zielStadt) {
        this.zielStadt = zielStadt;
    }

    public Land getZielLand() {return zielStadt.getLand();}

    public String getUni() {
        return uni;
    }

    public void setUni(String uni) {
        this.uni = uni;
    }

    public int getMaxUser() {
        return maxUser;
    }

    public void setMaxUser(int maxUser) {
        this.maxUser = maxUser;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Date getVonDat() {
        return vonDat;
    }

    public void setVonDat(Date vonDat) {
        this.vonDat = vonDat;
    }

    public Date getBisDat() {
        return bisDat;
    }

    public void setBisDat(Date bisDat) {
        this.bisDat = bisDat;
    }

    public Set<User> getInseratAnfragen() {
        return inseratAnfragen;
    }

    public void anfragen(User user) {
        inseratAnfragen.add(user);
    }

    public void anfrageEntfernen(Long userId) {
        inseratAnfragen.removeIf(user -> user.getUserId().equals(inseratId));
    }

    public void mitgliedEntfernen(Long mitgliedId) {
        mitglieder.removeIf(mitglied -> mitglied.getUserId().equals(mitgliedId));
    }

    public Set<User> getMitglieder() {
        return mitglieder;
    }
}
