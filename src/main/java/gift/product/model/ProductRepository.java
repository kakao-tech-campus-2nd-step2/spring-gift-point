package gift.product.model;

import gift.product.model.dto.product.Product;
import jakarta.persistence.Tuple;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p AS product, COUNT(w) AS wishCount FROM Product p LEFT JOIN Wish w ON w.product = p WHERE p.id = :id GROUP BY p")
    Optional<Tuple> findProductByIdWithWishCount(@Param("id") Long id);

    @Query("SELECT p AS product, COUNT(w) AS wishCount FROM Product p LEFT JOIN Wish w ON w.product = p GROUP BY p")
    Page<Tuple> findAllActiveProductsWithWishCountPageable(Pageable pageable);

    @Query("SELECT p AS product, COUNT(w) AS wishCount FROM Product p LEFT JOIN Wish w ON w.product = p WHERE p.category.id = :categoryId GROUP BY p")
    Page<Tuple> findActiveProductsByCategoryWithWishCount(Long categoryId, Pageable pageable);

}
