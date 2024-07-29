package gift.repository;

import gift.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<Option> findByProductIdAndName(Long productId, String name);
}
