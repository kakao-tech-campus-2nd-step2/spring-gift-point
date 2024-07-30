package gift.order.dto;

import gift.order.domain.Order;
import gift.order.domain.OrderCount;
import gift.order.domain.OrderMessage;

public record OrderResponseDto(Long id, OrderCount count, OrderMessage message, Long optionId) {
    public static OrderResponseDto orderToOrderResponseDto(Order order) {
        return new OrderResponseDto(order.getId(), order.getCount(), order.getMessage(), order.getOption().getId());
    }
}
