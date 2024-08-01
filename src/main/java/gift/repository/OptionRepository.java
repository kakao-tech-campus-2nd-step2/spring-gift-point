package gift.repository;

import gift.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<Option> findById(Long id);

    List<Option> findAllByProductId(Long productId);
}
