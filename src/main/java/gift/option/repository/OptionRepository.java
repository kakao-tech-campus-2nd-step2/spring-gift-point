package gift.option.repository;

import gift.global.MyCrudRepository;
import gift.option.domain.Option;
import gift.option.domain.OptionName;

import java.util.List;

public interface OptionRepository extends MyCrudRepository<Option, Long> {
    boolean existsById(Long id);

    List<Option> findByProductId(Long productId);

    boolean existsByName(OptionName name);
}
