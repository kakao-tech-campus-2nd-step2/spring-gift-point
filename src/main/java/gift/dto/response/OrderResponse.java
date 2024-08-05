package gift.dto.response;

import gift.entity.Order;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long optionId,
        Integer quantity,
        LocalDateTime orderDateTime,
        String message
) {
    public static OrderResponse fromOrder(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        );
    }
}
