package gift.option.dto;

import gift.option.domain.OptionCount;
import gift.option.domain.OptionName;

public record OptionRequestDto(OptionName name, OptionCount count, Long productId) {
    public OptionServiceDto toOptionServiceDto() {
        return new OptionServiceDto(null, name, count, productId);
    }

    public OptionServiceDto toOptionServiceDto(Long id) {
        return new OptionServiceDto(id, name, count, productId);
    }
}
