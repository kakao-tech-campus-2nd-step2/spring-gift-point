package gift.repository;

import gift.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface OptionRepository extends JpaRepository<Option, Long> {
    Set<Option> findByProductId(Long productId);
    Optional<Option> findByName(String name);
}
