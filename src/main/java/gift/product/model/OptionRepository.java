package gift.product.model;

import gift.product.model.dto.option.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findAllByProductId(Long productId);

    int countByProductId(Long productId);
}
