package gift.main.repository;

import gift.main.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {

    Optional<List<Option>> findAllByProductId(Long id);

    Optional<Option> findByProductIdAndOptionName(Long id, String optionName);

    void deleteAllByProductId(long id);
}
