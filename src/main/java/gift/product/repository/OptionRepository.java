package gift.product.repository;

import gift.product.dto.option.OptionResponse;
import gift.product.model.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    List<OptionResponse> findAllByProductId(Long productId);

    boolean existsByNameAndProductId(String name, Long productId);

    void deleteByIdAndProductId(Long id, Long productId);
}
