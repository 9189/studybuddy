package at.diplomarbeit.studybuddy.data.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="refresh_token")
public class RefreshToken {
    @Id
    @Column(name="token")
    private String token;

    @OneToOne
    @JoinColumn(name="uid")
    private User user;

    protected RefreshToken() {
    }

    public RefreshToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
