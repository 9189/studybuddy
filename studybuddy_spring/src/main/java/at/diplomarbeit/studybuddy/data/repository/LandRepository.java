package at.diplomarbeit.studybuddy.data.repository;

import at.diplomarbeit.studybuddy.data.entity.Land;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandRepository extends CrudRepository<Land, Long> {
    boolean existsByName(String s);
    Land getLandByName(String s);
}
