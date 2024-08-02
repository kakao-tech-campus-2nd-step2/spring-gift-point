package gift.doamin.product.repository;

import gift.doamin.product.entity.Option;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    boolean existsByProductIdAndName(Long productId, String name);

    Optional<Option> findByName(String name);
}
