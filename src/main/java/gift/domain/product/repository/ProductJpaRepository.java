package gift.domain.product.repository;

import gift.domain.product.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p left join fetch p.options where p.id = :productId")
    Optional<Product> findById(@Param("productId") Long productId);

    @Query("select p from Product p join fetch p.category")
    List<Product> findAll();

    @Query("select p from Product p join p.options o where o.id = :optionId")
    Optional<Product> findByOptionId(@Param("optionId") Long optionId);
}
