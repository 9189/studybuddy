package at.diplomarbeit.studybuddy.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="stadt")
public class Stadt {
    @Id
    @Column(name="sid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stadtId;

    private String name;

    @ManyToOne
    @JoinColumn(name="lid")
    @JsonIgnore
    private Land land;

    @OneToOne
    @JoinColumn(name="bid")
    private Bild bild;

    public Stadt(String name, Land land) {
        this.name = name;
        this.land = land;
    }

    protected Stadt() {
    }

    public Long getStadtId() {
        return stadtId;
    }

    public String getName() {
        return name;
    }

    public Land getLand() {
        return land;
    }
}
