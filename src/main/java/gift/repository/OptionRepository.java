package gift.repository;

import gift.model.Option;
import gift.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {
    @Query("SELECT p FROM Product p JOIN p.options o WHERE o.id = :optionId")
    Optional<Product> findProductByOptionId(@Param("optionId") Long optionId);
}
