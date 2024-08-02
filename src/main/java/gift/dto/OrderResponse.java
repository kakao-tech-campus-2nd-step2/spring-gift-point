package gift.dto;

import gift.domain.Order;
import java.time.LocalDateTime;

public record OrderResponse(Long id, Long optionId, int quantity, LocalDateTime orderDateTime, String message) {
    public static OrderResponse from(final Order order){
        Long optionId = order.getOptionId();
        return new OrderResponse(order.getId(), optionId, order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }
}
