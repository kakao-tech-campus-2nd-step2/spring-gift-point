package gift.repository;

import gift.domain.Option;
import gift.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByProduct(Product product);
}
