package gift.repository;

import gift.model.Option;
import gift.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option,Long> {

    public List<Option> findAllByProduct(Product product);

    public Optional<Option> findByName(String name);
}
