package gift.order.restapi.dto;

import gift.core.domain.order.Order;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long optionId,
        Integer quantity,
        LocalDateTime orderedAt,
        String message
) {
    public static OrderResponse of(Order order) {
        return new OrderResponse(
                order.id(),
                order.optionId(),
                order.quantity(),
                order.orderedAt(),
                order.message()
        );
    }
}
