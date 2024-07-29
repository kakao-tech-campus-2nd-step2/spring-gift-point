package gift.repository;

import gift.entity.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByProductId(Long productId);
    boolean existsByNameAndProductId(String name,Long productId);
}
