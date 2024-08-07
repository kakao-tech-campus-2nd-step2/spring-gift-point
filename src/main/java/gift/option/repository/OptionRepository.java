package gift.option.repository;

import gift.option.entity.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    boolean existsByNameAndProductId(String name, long productId);

    List<Option> findByProductId(long productId);
}
