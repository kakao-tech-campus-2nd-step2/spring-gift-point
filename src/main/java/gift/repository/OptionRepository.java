package gift.repository;

import gift.model.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByProductId(Long productId);
   Optional<Option> findByProductIdAndName(Long productId,String name);
   boolean existsByProductIdAndName(Long productId,String name);
}
