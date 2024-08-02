package gift.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.order.domain.OrderQuantity;
import gift.order.domain.OrderMessage;

public record OrderRequestDto(OrderQuantity quantity, OrderMessage message, @JsonProperty("option_id") Long optionId) {
    public OrderServiceDto toOrderServiceDto(Long memberId) {
        return new OrderServiceDto(null, quantity, message, memberId, optionId);
    }

    public OrderServiceDto toOrderServiceDto(Long id, Long memberId) {
        return new OrderServiceDto(id, quantity, message, memberId, optionId);
    }
}
