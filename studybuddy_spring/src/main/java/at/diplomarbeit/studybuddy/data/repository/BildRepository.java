package at.diplomarbeit.studybuddy.data.repository;

import at.diplomarbeit.studybuddy.data.entity.Bild;
import at.diplomarbeit.studybuddy.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BildRepository extends CrudRepository<Bild, Long> {
    Optional<Bild> findFirstByUser(User user);
    boolean existsByUrl(String url);
}
