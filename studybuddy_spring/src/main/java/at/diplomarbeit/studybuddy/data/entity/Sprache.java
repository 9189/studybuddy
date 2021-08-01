package at.diplomarbeit.studybuddy.data.entity;

import javax.persistence.*;

@Entity
@Table(name="sprache")
public class Sprache {
    @Id
    @Column(name="iso_code")
    private String sprachenId;

    private String name;

    @Column(name="native_name")
    private String nativeName;

    public Sprache(String sprachenId, String name, String nativeName) {
        this.sprachenId = sprachenId;
        this.name = name;
        this.nativeName = nativeName;
    }

    protected Sprache() {
    }

    public String getSprachenId() {
        return sprachenId;
    }

    public void setSprachenId(String sprachenId) {
        this.sprachenId = sprachenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }
}
