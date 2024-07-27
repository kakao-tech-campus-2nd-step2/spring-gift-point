package gift.option.repository;

import gift.option.domain.Option;
import gift.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionJpaRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByProduct(Product product);
    Optional<Option> findByProduct(Product product);

    Optional<Option> findById(Long id);
}
