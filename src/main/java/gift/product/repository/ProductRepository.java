package gift.product.repository;

import gift.category.domain.Category;
import gift.global.MyCrudRepository;
import gift.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends MyCrudRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.category = null WHERE p.category.id = :categoryId")
    void setCategoryNullByCategoryId(@Param("categoryId") Long categoryId);
}
