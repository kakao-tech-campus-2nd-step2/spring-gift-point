package gift.domain.order.dto;

import gift.domain.order.entity.Order;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    Long id,
    List<OrderItemResponse> orderItems,
    String recipientMessage,
    int totalPrice,
    LocalDateTime orderDateTime
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getOrderItems().stream().map(OrderItemResponse::from).toList(),
            order.getRecipientMessage(),
            order.getTotalPrice(),
            order.getOrderDateTime()
        );
    }
}
