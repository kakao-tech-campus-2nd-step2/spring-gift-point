package gift.administrator.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, long id);

    @Query("SELECT DISTINCT p.category.name FROM Product p WHERE p.category IS NOT NULL")
    List<String> findDistinctCategoryNamesWithProducts();
}
