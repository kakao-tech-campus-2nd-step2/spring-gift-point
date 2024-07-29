package gift.repository.product;

import gift.model.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long>, CategoryRepository {

}
