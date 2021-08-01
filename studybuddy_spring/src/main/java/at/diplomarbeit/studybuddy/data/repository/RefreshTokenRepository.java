package at.diplomarbeit.studybuddy.data.repository;

import at.diplomarbeit.studybuddy.data.entity.RefreshToken;
import at.diplomarbeit.studybuddy.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    @Transactional
    void deleteByUser(User user);
}
