package gift.core.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository {

    void save(Long productId, ProductOption option);

    List<ProductOption> findAllByProductId(Long productId);

    Optional<ProductOption> findById(Long optionId);

    Long getProductIdByOptionId(Long optionId);

    Long countByProductId(Long productId);

    boolean hasOption(Long productId, String optionName);

    boolean hasOption(Long optionId);

    void deleteById(Long id);

}
