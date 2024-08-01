package gift.dto.giftorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.dto.option.OptionResponse;
import gift.dto.product.ProductBasicInformation;

import java.time.LocalDateTime;

public record GiftOrderResponse(
        Long id,
        @JsonProperty("product")
        ProductBasicInformation productBasicInformation,
        @JsonProperty("option")
        OptionResponse optionResponse,
        Integer quantity,
        LocalDateTime orderDateTime,
        String message
) {
    public static GiftOrderResponse of(Long id, ProductBasicInformation productBasicInformation, OptionResponse optionResponse, Integer quantity, LocalDateTime orderDateTime, String message) {
        return new GiftOrderResponse(id, productBasicInformation, optionResponse, quantity, orderDateTime, message);
    }
}
