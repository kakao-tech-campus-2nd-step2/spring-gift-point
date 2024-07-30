package gift.repository;

import gift.domain.Option;
import gift.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findByProduct(Product product);

    Optional<Option> findByProductAndId(Product product, long id);
}
