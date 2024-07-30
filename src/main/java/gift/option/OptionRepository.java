package gift.option;

import gift.option.model.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findAllByProductId(Long productId);

    void deleteById(Long id);
}
