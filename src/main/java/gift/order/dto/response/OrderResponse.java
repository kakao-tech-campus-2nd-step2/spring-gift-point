package gift.order.dto.response;

import gift.order.entity.Order;
import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    Long optionId,
    Integer quantity,
    LocalDateTime orderDateTime,
    String message
) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getOptionId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }

}
