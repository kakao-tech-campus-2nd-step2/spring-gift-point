package gift.repository;

import gift.domain.option.Option;
import gift.domain.product.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findAllByProduct(Product product);

    Optional<Option> findAllByProductAndName(Product product, String name);
}
