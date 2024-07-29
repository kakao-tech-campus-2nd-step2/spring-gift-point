package gift.controller.order;

import gift.domain.Option;
import gift.domain.Order;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderMapper {

    public static Order toOrder(Option option, LocalDateTime orderDateTime, String message) {
        return new Order(option, orderDateTime, message);
    }

    public static OrderResponse toOrderResponse(UUID orderId, UUID optionId, Integer quantity,
        LocalDateTime orderDateTime, String message) {
        return new OrderResponse(orderId, optionId, quantity, orderDateTime, message);
    }
}