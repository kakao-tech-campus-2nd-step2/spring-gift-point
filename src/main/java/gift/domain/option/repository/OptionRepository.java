package gift.domain.option.repository;

import gift.domain.option.entity.Option;
import gift.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findAllByProduct(Product product);

    void deleteByProduct(Product product);
}
