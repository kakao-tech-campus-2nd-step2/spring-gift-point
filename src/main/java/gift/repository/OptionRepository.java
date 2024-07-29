package gift.repository;

import gift.entity.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    @Query("SELECT o FROM Option o JOIN FETCH o.product WHERE o.product.id = :productId")
    List<Option> findByProductId(@Param("productId") Long productId);

    @Query("SELECT o FROM Option o JOIN FETCH o.product WHERE o.id = :id")
    Optional<Option> findWithId(@Param("id") Long id);

    Optional<Option> findByIdAndProductId(Long optionId, Long productId);

}
