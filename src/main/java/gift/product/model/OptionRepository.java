package gift.product.model;

import gift.product.model.dto.option.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<Option> findByIdAndIsActiveTrue(Long id);

    List<Option> findAllByProductIdAndIsActiveTrue(Long productId);

    int countByProductIdAndIsActiveTrue(Long productId);
}
