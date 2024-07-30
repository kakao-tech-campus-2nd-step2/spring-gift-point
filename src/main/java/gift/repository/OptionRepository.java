package gift.repository;

import gift.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<Option> findByIdAndProductId(Long optionId, Long productId);
    Optional<Option> findByName(String name);
}