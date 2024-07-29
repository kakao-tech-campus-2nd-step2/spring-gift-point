package gift.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import gift.order.entity.Order;
import java.time.LocalDateTime;

public record OrderResDto(
        Long id,
        Long optionId,
        Integer quantity,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime orderDateTime,
        String message
) {

    public OrderResDto(Order order) {
        this(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }
}
