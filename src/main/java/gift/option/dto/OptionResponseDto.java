package gift.option.dto;

import gift.option.domain.Option;
import gift.option.domain.OptionQuantity;
import gift.option.domain.OptionName;

public record OptionResponseDto(Long id, OptionName name, OptionQuantity count, Long productId) {
    public static OptionResponseDto optionToOptionResponseDto(Option option) {
        return new OptionResponseDto(option.getId(), option.getName(), option.getQuantity(), option.getProduct().getId());
    }
}
