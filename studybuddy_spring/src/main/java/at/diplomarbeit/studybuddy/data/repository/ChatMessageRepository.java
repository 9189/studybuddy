package at.diplomarbeit.studybuddy.data.repository;

import at.diplomarbeit.studybuddy.data.entity.ChatMessage;
import at.diplomarbeit.studybuddy.data.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ChatMessageRepository extends PagingAndSortingRepository<ChatMessage, Long> {
    Iterable<ChatMessage> findAllBySenderAndRecipientsIn(User user, Collection<User> userSet);
}
