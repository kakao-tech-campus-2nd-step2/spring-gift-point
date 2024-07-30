package gift.controller.order;

import gift.domain.Option;
import gift.domain.Order;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderMapper {

    public static Order toOrder(Option option, Integer quantity, LocalDateTime orderDateTime, String message) {
        return new Order(option, quantity, orderDateTime, message);
    }

    public static OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }
}