package gift.category.repository;

import gift.category.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean deleteByName(String name);

    List<Category> findByProductListIsNotEmpty();
}