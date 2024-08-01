package gift.order.dto;

import gift.order.domain.Order;
import gift.order.domain.OrderQuantity;
import gift.order.domain.OrderMessage;

public record OrderResponseDto(Long id, OrderQuantity count, OrderMessage message, Long optionId) {
    public static OrderResponseDto orderToOrderResponseDto(Order order) {
        return new OrderResponseDto(order.getId(), order.getQuantity(), order.getMessage(), order.getOption().getId());
    }
}
