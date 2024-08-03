package gift.domain.order.dto;

import gift.domain.order.entity.Order;
import java.time.LocalDateTime;
import java.util.List;

public record MultipleOrderResponse(
    Long id,
    List<OrderItemResponse> orderItems,
    String recipientMessage,
    int originalPrice,
    int finalPrice,
    LocalDateTime orderDateTime
) {
    public static MultipleOrderResponse from(Order order) {
        return new MultipleOrderResponse(
            order.getId(),
            order.getOrderItems().stream().map(OrderItemResponse::from).toList(),
            order.getRecipientMessage(),
            order.getOriginalPrice(),
            order.getFinalPrice(),
            order.getOrderDateTime()
        );
    }
}
