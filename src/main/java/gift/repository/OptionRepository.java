package gift.repository;

import gift.entity.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    boolean existsByNameAndProductId(String name, Long productId);

    boolean existsByIdAndProductId(Long id, Long productId);

    List<Option> findByProductId(Long productId);
}