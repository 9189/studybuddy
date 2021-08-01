package at.diplomarbeit.studybuddy.data.repository;

import at.diplomarbeit.studybuddy.data.entity.Stadt;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StadtRepository extends CrudRepository<Stadt, Long> {
    boolean existsByName(String s);
}
