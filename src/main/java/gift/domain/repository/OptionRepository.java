package gift.domain.repository;

import gift.domain.model.entity.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    boolean existsByName(String name);

    List<Option> findAllByProductId(Long productId);
}
