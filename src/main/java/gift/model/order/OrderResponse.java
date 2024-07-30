package gift.model.order;

import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    Long product_id,
    Long option_id,
    int quantity,
    LocalDateTime orderDateTime,
    String message
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getProductId(),
            order.getOption().getId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }
}
