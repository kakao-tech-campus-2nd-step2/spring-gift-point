package gift.api.option.dao;

import gift.api.option.domain.Option;
import gift.api.option.repository.OptionRepository;
import gift.global.exception.NoSuchEntityException;
import org.springframework.stereotype.Repository;

@Repository
public class OptionDao {

    private final OptionRepository optionRepository;

    public OptionDao(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public Option findOptionById(Long id) {
        return optionRepository.findById(id)
            .orElseThrow(() -> new NoSuchEntityException("option"));
    }
}
