package gift.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.model.Order;
import java.time.format.DateTimeFormatter;

public record OrderResponse(
    @JsonProperty(value = "order_id")
    Long id,
    @JsonProperty(value = "option_id")
    Long optionId,
    Integer quantity,
    @JsonProperty(value = "order_date_time")
    String orderDateTime,
    String message
) {

    private OrderResponse(Order order) {
        this(order.getId(), order.getOptions().getId(), order.getQuantity(),
            order.getCreatedAt().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), order.getMessage());
    }

    public static OrderResponse createOrderResponse(Order order) {
        return new OrderResponse(order);
    }
}
