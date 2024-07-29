package gift.core.domain.product;

import java.util.List;

public interface ProductOptionService {

    void registerOptionToProduct(Long productId, ProductOption productOption);

    List<ProductOption> getOptionsFromProduct(Long productId);

    void removeOptionFromProduct(Long productId, Long optionId);

    void subtractQuantityFromOption(Long optionId, Integer quantity);

}
