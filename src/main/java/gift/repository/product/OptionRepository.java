package gift.repository.product;

import gift.model.product.Option;
import gift.model.product.Options;
import java.util.List;
import java.util.Optional;

public interface OptionRepository {

    void deleteById(Long id);

    Optional<Option> findById(Long id);

    Option save(Option option);

    Options findAllByProductId(Long productId);

    boolean existsById(Long optionId);

    void saveAllByOptions(Options options);

}
