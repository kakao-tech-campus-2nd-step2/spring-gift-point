package gift.domain.repository;

import gift.domain.dto.request.ProductAddRequest;
import gift.domain.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p "
        + "FROM Product p "
        + "WHERE p.name = :#{#request.name()} "
        + "    AND p.price = :#{#request.price()} "
        + "    AND p.imageUrl = :#{#request.imageUrl()}")
    Optional<Product> findByContents(ProductAddRequest request);
}
