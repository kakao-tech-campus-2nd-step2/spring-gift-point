package gift.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OrderResponse(
    Long id,
    Long optionId,
    int quantity,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime orderDateTime,
    String message
) {
    public static OrderResponse from(OrderInfo order) {
        return new OrderResponse(
            order.getId(), order.getOptionId(), order.getQuantity(), order.getOrderDateTime(),
            order.getMessage()
        );
    }
}
