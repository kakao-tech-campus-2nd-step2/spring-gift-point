package gift.order.dto;

import gift.order.domain.OrderCount;
import gift.order.domain.OrderMessage;

public record OrderRequestDto(OrderCount count, OrderMessage message, Long optionId) {
    public OrderServiceDto toOrderServiceDto(Long memberId) {
        return new OrderServiceDto(null, count, message, memberId, optionId);
    }

    public OrderServiceDto toOrderServiceDto(Long id, Long memberId) {
        return new OrderServiceDto(id, count, message, memberId, optionId);
    }
}
