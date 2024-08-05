package gift.order.restapi.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.core.domain.order.Order;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OrderRequest(
        Long optionId,
        Integer quantity,
        Long point,
        String message
) {
    public Order toOrder(Long userId) {
        return Order.newOrder(userId, optionId(), point(), quantity(), message());
    }
}
