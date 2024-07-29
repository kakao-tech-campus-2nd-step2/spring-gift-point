package gift.option;

import gift.option.entity.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findAllByProductId(long productId);

    boolean existsByNameAndProductId(String optionName, long productId);
}
