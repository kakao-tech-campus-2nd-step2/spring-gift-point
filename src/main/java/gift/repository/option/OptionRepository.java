package gift.repository.option;

import gift.model.option.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
