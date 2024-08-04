package gift.administrator.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, long id);

    @Query("SELECT DISTINCT c.name FROM Category c WHERE c.id = :categoryId")
    String findDistinctCategoryNameWithCategoryId(@Param("categoryId") Long categoryId);

    Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);
}
