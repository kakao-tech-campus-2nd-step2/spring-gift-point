package gift.option.repository;

import gift.option.domain.Option;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository {
    Option findById(Long id);
    void save(Option option);
}
