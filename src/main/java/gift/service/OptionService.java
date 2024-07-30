package gift.service;

import gift.model.Option;
import java.util.List;

public interface OptionService {
    Option addOptionToProduct(Long productId, Option option);
    List<Option> getOptionsByProduct(Long productId);
    void subtractOptionQuantity(Long optionId, int quantity);
}
