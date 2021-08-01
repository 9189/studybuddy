package at.diplomarbeit.studybuddy.controller;

import at.diplomarbeit.studybuddy.data.entity.ChatMessage;
import at.diplomarbeit.studybuddy.data.entity.User;
import at.diplomarbeit.studybuddy.data.repository.ChatMessageRepository;
import at.diplomarbeit.studybuddy.data.repository.UserRepository;
import at.diplomarbeit.studybuddy.dto.ChatMessageDTO;
import at.diplomarbeit.studybuddy.util.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ChatController {
    private SimpMessagingTemplate simpMessagingTemplate;
    private ChatMessageRepository chatMessageRepository;
    private UserRepository userRepository;
    private UserUtility userUtility;

    @Autowired
    public ChatController(SimpMessagingTemplate simpMessagingTemplate,
                          ChatMessageRepository chatMessageRepository,
                          UserRepository userRepository,
                          UserUtility userUtility) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.userUtility = userUtility;
    }

    @MessageMapping("/chat/user/{id}")
    public void sendToUser(@DestinationVariable Long id, ChatMessageDTO message) {
        if(userRepository.existsById(id)) {

            ChatMessage chatMessage = new ChatMessage(
                    userRepository.findById(message.getSenderId()).orElseThrow(),
                    message.getRecipientIds().stream().map(recipientId -> userRepository.findById(recipientId).orElseThrow()).collect(Collectors.toSet()),
                    message.getContent()
            );

            chatMessageRepository.save(chatMessage);
            simpMessagingTemplate.convertAndSend("/topic/user/messages/" + id, message);
        }
    }

    @MessageMapping("/chat/gruppe/{id}")
    public void sendToGroup(@DestinationVariable Long id, ChatMessageDTO message) {
    }

    @GetMapping(path = "/messages")
    public Iterable<ChatMessage> findUserChatMessages(HttpServletRequest request,
                                                      @RequestParam(name = "contact") Long contactId) {
        String token = userUtility.getTokenFromHeader(request.getHeader("Authorization"));
        User user = userUtility.getUserFromToken(token);

        User contact = userRepository.findById(contactId).orElseThrow();

        ArrayList<ChatMessage> sentMessages = (ArrayList<ChatMessage>) chatMessageRepository.findAllBySenderAndRecipientsIn(
                user,
                new HashSet<>(Set.of(contact))
        );

        ArrayList<ChatMessage> recievedMessages = (ArrayList<ChatMessage>) chatMessageRepository.findAllBySenderAndRecipientsIn(
                contact,
                new HashSet<>(Set.of(user))
        );

        sentMessages.addAll(recievedMessages);

        Collections.sort(sentMessages, new Comparator<ChatMessage>() {
            @Override
            public int compare(ChatMessage o1, ChatMessage o2) {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        });

        return sentMessages;
    }

    @GetMapping(path = "/group-messages")
    public Iterable<ChatMessage> findGroupChatMessages(@RequestParam(name = "sender-id") Long senderId,
                                                      @RequestParam(name = "group-id") Long groupId) {
        return null;
    }
}
