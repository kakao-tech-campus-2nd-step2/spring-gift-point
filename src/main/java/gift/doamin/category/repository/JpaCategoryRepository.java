package gift.doamin.category.repository;

import gift.doamin.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {

}
