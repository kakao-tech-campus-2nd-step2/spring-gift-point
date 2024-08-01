package gift.repository;

import gift.model.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findALlByProductId(Long productId);
   Optional<Option> findByProductIdAndOptionName(Long productId,String name);
   boolean existsByProductIdAndOptionName(Long productId,String name);
}
