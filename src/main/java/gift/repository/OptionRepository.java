package gift.repository;

import gift.domain.Option;
import gift.domain.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, UUID> {

    List<Option> findAllByProduct(Product product);
    Optional<Option> findByNameAndProduct(String optionName, Product product);
}