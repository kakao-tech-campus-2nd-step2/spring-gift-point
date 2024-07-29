package gift.controller.option;

import gift.domain.Option;

public class OptionMapper {
    public static OptionResponse toOptionResponse(Option option) {
        return new OptionResponse(option.getId(), option.getName(), option.getQuantity(), option.getProductId());
    }
}
