package gift.api.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.api.order.domain.Order;
import java.sql.Timestamp;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OrderResponse(
    Long id,
    Long optionId,
    Integer quantity,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Timestamp orderDateTime,
    String message,
    Integer point
) {

    public static OrderResponse of(Order order, Integer point) {
        return new OrderResponse(order.getId(),
            order.getOptionId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage(),
            point);
    }
}
