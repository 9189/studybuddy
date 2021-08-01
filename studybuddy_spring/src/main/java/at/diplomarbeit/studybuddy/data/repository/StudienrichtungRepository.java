package at.diplomarbeit.studybuddy.data.repository;

import at.diplomarbeit.studybuddy.data.entity.Studienrichtung;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudienrichtungRepository extends CrudRepository<Studienrichtung, Long> {
    boolean existsByBezeichnungDeutsch(String s);
}
