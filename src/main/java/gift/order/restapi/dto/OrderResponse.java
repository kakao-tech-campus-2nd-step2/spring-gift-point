package gift.order.restapi.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.core.domain.order.Order;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
