package gift.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.model.Order;
import java.time.format.DateTimeFormatter;

public record CreatedOrderResponse (
    @JsonProperty(value = "order_id")
    Long id,
    @JsonProperty(value = "option_id")
    Long optionId,
    Integer quantity,
    @JsonProperty(value = "order_date_time")
    String orderDateTime,
    String message,
    @JsonProperty(value = "remain_point")
    Integer remainPoint
) {

    private CreatedOrderResponse(Order order, Integer remainPoint) {
            this(order.getId(), order.getOptions().getId(), order.getQuantity(),
                order.getCreatedAt().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                order.getMessage(), remainPoint);
        }

        public static CreatedOrderResponse createOrderResponse(Order order, Integer remainPoint) {
            return new CreatedOrderResponse(order,remainPoint);
        }
}
