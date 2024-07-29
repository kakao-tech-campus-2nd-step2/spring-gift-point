package gift.controller.order.dto;

import gift.model.Order;
import java.time.LocalDateTime;

public record OrderResponse(Long id, Long optionId, int quantity, LocalDateTime orderDateTime,
                            String message) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getOption().getId(), order.getQuantity(),
            order.getCreatedAt(),
            order.getMessage());
    }
}
