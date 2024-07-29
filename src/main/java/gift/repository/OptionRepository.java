package gift.repository;

import gift.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    Option findOptionByName(String optionName);

    Option findByProductId(Long ProductId);
}