package at.diplomarbeit.studybuddy.data.repository;

import at.diplomarbeit.studybuddy.data.entity.Inserat;
import at.diplomarbeit.studybuddy.data.entity.Land;
import at.diplomarbeit.studybuddy.data.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InseratRepository extends PagingAndSortingRepository<Inserat, Long>, QuerydslPredicateExecutor<Inserat> {
    Optional<Inserat> findByErsteller(User user);
    Boolean existsByErsteller(User user);
}
