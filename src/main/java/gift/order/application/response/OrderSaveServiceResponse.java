package gift.order.application.response;

import gift.order.domain.Order;

import java.time.LocalDateTime;

public record OrderSaveServiceResponse(
        Long id,
        LocalDateTime orderDateTime,
        Integer originalPrice,
        Integer finalPrice
) {
    public static OrderSaveServiceResponse of(Order order, Integer originalPrice, Integer finalPrice) {
        return new OrderSaveServiceResponse(order.getId(), order.getOrderDateTime(), originalPrice, finalPrice);
    }
}
