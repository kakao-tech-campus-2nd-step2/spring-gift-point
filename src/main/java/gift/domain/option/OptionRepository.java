package gift.domain.option;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    @EntityGraph(attributePaths = {"product"})
    List<Option> findAllByProductId(Long productId);

    @EntityGraph(attributePaths = {"product"})
    Optional<Option> findByIdAndProductId(Long id, Long productId);
}
