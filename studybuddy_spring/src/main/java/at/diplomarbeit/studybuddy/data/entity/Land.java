package at.diplomarbeit.studybuddy.data.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="land")
public class Land {
    @Id
    @Column(name="lid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long landId;

    private String name;

    @OneToMany(mappedBy = "land")
    private Set<Stadt> staedte;

    protected Land() {
    }

    public Land(String name) {
        this.name = name;
    }

    public Long getLandId() {
        return landId;
    }

    public String getName() {
        return name;
    }

    public Set<Stadt> getStaedte() {
        return staedte;
    }
}
