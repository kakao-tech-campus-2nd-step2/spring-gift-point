package gift.administrator.option;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findAllByProductId(long productId);

    boolean existsByIdAndProductId(long optionId, long productId);

    boolean existsByNameAndProductIdAndIdNot(String name, long productId, long optionId);

    int countAllByProductId(Long productId);
}
