package gift.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.model.Order;
import java.time.format.DateTimeFormatter;

public record CreatedOrderResponse(
    @JsonProperty(value = "order_id")
    Long id,
    @JsonProperty(value = "option_id")
    Long optionId,
    Integer quantity,
    @JsonProperty(value = "order_date_time")
    String orderDateTime,
    String message,
    @JsonProperty(value = "used_point")
    Integer usedPoint
) {

    private CreatedOrderResponse(Order order, Integer usedPoint) {
        this(order.getId(), order.getOptions().getId(), order.getQuantity(),
            order.getCreatedAt().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            order.getMessage(), usedPoint);
    }

    public static CreatedOrderResponse createOrderResponse(Order order, Integer usedPoint) {
        return new CreatedOrderResponse(order, usedPoint);
    }
}
