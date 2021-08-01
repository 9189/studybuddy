package at.diplomarbeit.studybuddy.data.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sid")
    private User sender;

    @ManyToMany
    @JoinTable(name = "chat_message_empfaenger",
            joinColumns = {@JoinColumn(name = "cid")},
            inverseJoinColumns = {@JoinColumn(name = "uid")})
    private Set<User> recipients;

    private String content;
    private LocalDateTime timestamp;

    public ChatMessage(User sender, Set<User> recipients, String content) {
        this.sender = sender;
        this.recipients = recipients;
        this.content = content;

        this.timestamp = LocalDateTime.now();
    }

    protected ChatMessage() {
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public Set<User> getRecipients() {
        return recipients;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
