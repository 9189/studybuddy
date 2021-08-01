package at.diplomarbeit.studybuddy.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="bild")
public class Bild {
    @Id
    @Column(name="bid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bildId;

    private Date datum;

    private String url;

    @ManyToOne
    @JoinColumn(name="uid")
    @JsonIgnore
    private User user;

    public Bild(Date datum, String url, User user) {
        this.datum = datum;
        this.url = url;
        this.user = user;
    }

    public Bild() {

    }

    public Long getBildId() {
        return bildId;
    }

    public String getUrl() {
        return url;
    }
}
