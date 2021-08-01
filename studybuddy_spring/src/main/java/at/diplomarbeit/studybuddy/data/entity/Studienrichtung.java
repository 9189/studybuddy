package at.diplomarbeit.studybuddy.data.entity;

import javax.persistence.*;

@Entity
@Table(name="studienrichtung")
public class Studienrichtung {
    @Id
    @Column(name="stu_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stuId;

    @Column(name="bezeichnung_deutsch")
    private String bezeichnungDeutsch;

    @Column(name="bezeichnung_englisch")
    private String bezeichnungEnglisch;

    public Studienrichtung(String bezeichnungDeutsch, String bezeichnungEnglisch) {
        this.bezeichnungDeutsch = bezeichnungDeutsch;
        this.bezeichnungEnglisch = bezeichnungEnglisch;
    }

    protected Studienrichtung() {
    }

    public Long getStuId() {
        return stuId;
    }

    public String getBezeichnungDeutsch() {
        return bezeichnungDeutsch;
    }

    public void setBezeichnungDeutsch(String bezeichnungDeutsch) {
        this.bezeichnungDeutsch = bezeichnungDeutsch;
    }

    public String getBezeichnungEnglisch() {
        return bezeichnungEnglisch;
    }

    public void setBezeichnungEnglisch(String bezeichnungEnglisch) {
        this.bezeichnungEnglisch = bezeichnungEnglisch;
    }
}
