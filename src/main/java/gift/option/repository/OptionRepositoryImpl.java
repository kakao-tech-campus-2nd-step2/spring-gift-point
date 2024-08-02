package gift.option.repository;

import gift.option.domain.Option;
import org.springframework.stereotype.Repository;

@Repository
public class OptionRepositoryImpl implements OptionRepository {
    private final OptionJpaRepository optionJpaRepository;

    public OptionRepositoryImpl(OptionJpaRepository optionJpaRepository) {
        this.optionJpaRepository = optionJpaRepository;
    }

    @Override
    public Option findById(Long id) {
        return optionJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 id의 옵션은 존재하지 않음 : " + id));
    }

    @Override
    public void save(Option option) {
        optionJpaRepository.save(option);
    }
}
