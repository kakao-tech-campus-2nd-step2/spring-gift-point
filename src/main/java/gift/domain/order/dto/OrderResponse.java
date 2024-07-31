package gift.domain.order.dto;

import gift.domain.order.entity.Order;
import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    Long optionId,
    int quantity,
    LocalDateTime orderDateTime,
    String message
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getOrderItems().getFirst().getId(),
            order.getOrderItems().getFirst().getQuantity(),
            order.getOrderDateTime(),
            order.getRecipientMessage()
        );
    }
}
