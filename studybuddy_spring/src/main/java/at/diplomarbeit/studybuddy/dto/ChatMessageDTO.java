package at.diplomarbeit.studybuddy.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class ChatMessageDTO {
    private Long senderId;
    private Set<Long> recipientIds;
    private String content;

    public ChatMessageDTO(Long senderId, Set<Long> recipientIds, String content) {
        this.senderId = senderId;
        this.recipientIds = recipientIds;
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Set<Long> getRecipientIds() {
        return recipientIds;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return LocalDateTime.now();
    }
}
