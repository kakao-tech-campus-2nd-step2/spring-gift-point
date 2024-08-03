package gift.service;

import gift.model.Option;

import java.util.List;

public interface OptionService {
    Option addOptionToProduct(Long productId, Option option);
    Option updateOption(Long productId, Long optionId, Option option);
    void deleteOption(Long productId, Long optionId);
    List<Option> getOptionsByProduct(Long productId);
}