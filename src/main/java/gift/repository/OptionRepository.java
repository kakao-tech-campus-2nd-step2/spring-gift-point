package gift.repository;

import gift.vo.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<List<Option>> findAllByProductId(Long productId);
    Optional<Option> findByNameAndProductId(String name, Long productId);
}
