package gift.repository;

import gift.entity.Option;
import gift.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findAllByProductId(Long productId);

    @Query("SELECT o.product FROM Option o WHERE o.id = :optionId")
    Optional<Product> findProductByOptionId(@Param("optionId") Long optionId);

}
