package gift.repository;

import gift.entity.Option;
import gift.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    Boolean existsByProduct(Product product);

    Boolean existsByProductAndName(Product product, String name);

    Optional<Option> findByIdAndProductId(Long id, Long productId);

    Optional<Option> findById(Long id);

    List<Option> findAllByProductId(Long id);

    Integer countByProduct(Product product);

    void deleteAllByProductId(Long id);
}
