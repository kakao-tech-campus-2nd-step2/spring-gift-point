package gift.domain.repository;

import gift.domain.entity.Option;
import gift.domain.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findAllByProduct(Product product);
}
