package gift.product.dao;

import gift.product.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {

    Optional<Option> findByProduct_IdAndName(Long productId, String name);

}