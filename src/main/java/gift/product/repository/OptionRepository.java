package gift.product.repository;

import gift.product.entity.Option;
import gift.product.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
  boolean existsByProductIdAndName(Long productId, String name);

  List<Option> findByProductId(Long productId);

  List<Option> findAllByProduct(Product product);

  Optional<Option> findByIdAndProductId(Long id, Long productId);

}
