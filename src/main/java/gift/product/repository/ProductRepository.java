package gift.product.repository;

import gift.category.domain.Category;
import gift.product.domain.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> getProductsByCategoryId(Long categoryId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.name = :name, p.price = :price, p.imageUrl = :imageUrl, p.category = :category WHERE p.id = :id")
    Product updateProduct(
        @Param("id") Long id,
        @Param("name") String name,
        @Param("price") Long price,
        @Param("imageUrl") String imageUrl,
        @Param("category") Category category);
}
