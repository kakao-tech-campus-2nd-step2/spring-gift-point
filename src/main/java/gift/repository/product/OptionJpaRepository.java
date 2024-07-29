package gift.repository.product;

import gift.model.product.Option;
import gift.model.product.Options;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionJpaRepository extends JpaRepository<Option, Long>, OptionRepository {

    @Override
    default void saveAllByOptions(Options options) {
        saveAll(options.getOptions());
    }

    List<Option> findByProductId(Long productId);

    @Override
    default Options findAllByProductId(Long productId) {
        return new Options(this.findByProductId(productId));
    }
}
