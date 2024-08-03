package gift.option.repository;

import gift.option.model.Option;
import gift.product.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option,Long> {

    public List<Option> findAllByProduct(Product product);

    public Optional<Option> findByName(String name);
}
