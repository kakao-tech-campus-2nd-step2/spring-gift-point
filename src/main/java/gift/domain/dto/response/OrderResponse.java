package gift.domain.dto.response;

import gift.domain.entity.Order;
import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    Long optionId,
    Integer quantity,
    LocalDateTime orderDateTime,
    String message
) {

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }
}
