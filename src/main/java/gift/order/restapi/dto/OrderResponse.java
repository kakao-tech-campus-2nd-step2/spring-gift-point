package gift.order.restapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.core.domain.order.Order;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OrderResponse(
        Long id,
        Long optionId,
        Long usedPoint,
        Integer quantity,
        @Schema(description = "기간 종료일", example = "2024-08-02 00:00:00", type = "string")
        @JsonProperty("order_date_time")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime orderedAt,
        String message
) {
    public static OrderResponse of(Order order) {
        return new OrderResponse(
                order.id(),
                order.optionId(),
                order.usedPoint(),
                order.quantity(),
                order.orderedAt(),
                order.message()
        );
    }
}
