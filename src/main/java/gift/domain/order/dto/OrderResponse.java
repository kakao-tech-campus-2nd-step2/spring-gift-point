package gift.domain.order.dto;

import gift.domain.order.entity.Order;
import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    Long optionId,
    int quantity,
    String message,
    int originalPrice,
    int finalPrice,
    LocalDateTime orderDateTime
) {
    public static OrderResponse from(Order order, int originalPrice) {
        return new OrderResponse(
            order.getId(),
            order.getOrderItems().getFirst().getId(),
            order.getOrderItems().getFirst().getQuantity(),
            order.getRecipientMessage(),
            originalPrice,
            order.getPurchasePrice(),
            order.getOrderDateTime()
        );
    }
}
