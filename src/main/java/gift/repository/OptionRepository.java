package gift.repository;

import gift.domain.Option;
import gift.entity.OptionEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Long> {

    OptionEntity save(Option option);

    boolean existsByNameAndIdNot(String name, Long id);

}
