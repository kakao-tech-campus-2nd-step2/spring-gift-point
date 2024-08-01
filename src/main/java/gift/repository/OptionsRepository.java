package gift.repository;

import gift.entity.Option;
import gift.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionsRepository extends JpaRepository<Options, Long> {
    Optional<Options> findByProduct_Id(int product_id);
    Optional<Integer> findProductIdByOptionListContaining(Option option);
}