package gift.repository;

import gift.model.Option;
import gift.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByProduct(Product product);
}
