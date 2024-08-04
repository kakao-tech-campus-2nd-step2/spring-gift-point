package gift.product.option.repository;

import gift.product.option.domain.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByProductId(Long productId);
    Optional<Option> findByProductIdAndName(Long productId, String name);
}
