package gift.option.dto;

import gift.option.domain.Option;
import gift.option.domain.OptionCount;
import gift.option.domain.OptionName;

public record OptionResponseDto(Long id, OptionName name, OptionCount count, Long productId) {
    public static OptionResponseDto optionToOptionResponseDto(Option option) {
        return new OptionResponseDto(option.getId(), option.getName(), option.getCount(), option.getProduct().getId());
    }
}
