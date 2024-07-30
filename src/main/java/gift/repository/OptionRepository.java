package gift.repository;

import gift.model.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findAllByProductId(Long productId);

    void deleteAllByProductIdIn(List<Long> productIds);

    boolean existsByProductIdAndName(Long productId, String name);
}
