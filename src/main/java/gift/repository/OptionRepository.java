package gift.repository;

import gift.model.Option;
import gift.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByProduct(Product product);
}
