package gift.repository;

import gift.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaDao extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String categoryName);

}
