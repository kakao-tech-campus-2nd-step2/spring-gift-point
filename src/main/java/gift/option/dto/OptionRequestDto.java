package gift.option.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.option.domain.OptionQuantity;
import gift.option.domain.OptionName;

public record OptionRequestDto(OptionName name, OptionQuantity quantity, @JsonProperty("product_id") Long productId) {
    public OptionServiceDto toOptionServiceDto() {
        return new OptionServiceDto(null, name, quantity, productId);
    }

    public OptionServiceDto toOptionServiceDto(Long id) {
        return new OptionServiceDto(id, name, quantity, productId);
    }
}
