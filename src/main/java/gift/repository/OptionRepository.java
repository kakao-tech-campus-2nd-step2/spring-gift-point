package gift.repository;

import gift.entity.Option;
import gift.entity.OptionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<Option> findByName(OptionName name);
}
