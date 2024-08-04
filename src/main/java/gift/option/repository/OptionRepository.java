package gift.option.repository;

import gift.option.domain.Option;

import java.util.Optional;

public interface OptionRepository {
    public Option findById(Long id);

    public void save(Option option);
}
