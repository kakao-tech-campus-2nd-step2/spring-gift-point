package gift.repository;

import gift.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {
    boolean existsByName(String name);
    List<Option> findByProductId(Long productId);
}
