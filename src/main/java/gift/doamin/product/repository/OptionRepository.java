package gift.doamin.product.repository;

import gift.doamin.product.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    public boolean existsByProductIdAndName(Long productId, String name);
}
