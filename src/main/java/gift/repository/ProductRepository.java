package gift.repository;

import gift.domain.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.options WHERE p.id = :id")
    Optional<Product> findByIdWithCategoryAndOption(@Param("id") Long id);

    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.options")
    Page<Product> findAllWithCategoryAndOption(Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.options WHERE p.id = :id")
    Optional<Product> findByIdWithOption(@Param("id") Long id);
}
