package gift.order.controller.response;

import java.time.LocalDateTime;

public record OrderCreateResponse(
        Long id,
        Long optionId,
        Integer quantity,
        String message,
        LocalDateTime orderDateTime,
        Integer originalPrice,
        Integer finalPrice
) {
    public static OrderCreateResponse of(
            Long id,
            Long optionId,
            Integer quantity,
            String message,
            LocalDateTime orderDateTime,
            Integer originalPrice,
            Integer finalPrice
    ) {
        return new OrderCreateResponse(id, optionId, quantity, message, orderDateTime, originalPrice, finalPrice);
    }
}
