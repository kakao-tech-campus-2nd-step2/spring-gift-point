package gift.controller.dto.response;

import gift.model.Orders;

import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        Long productId,
        Long optionId,
        int quantity,
        LocalDateTime orderDateTime,
        String message
) {
    public static OrderResponse from(Orders orders) {
        return new OrderResponse(
                orders.getId(), orders.getProductId(), orders.getOptionId(),
                orders.getQuantity(), orders.getCreatedAt(), orders.getDescription()
        );
    }
}
