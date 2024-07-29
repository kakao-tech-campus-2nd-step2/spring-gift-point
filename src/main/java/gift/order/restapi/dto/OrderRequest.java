package gift.order.restapi.dto;

import gift.core.domain.order.Order;

public record OrderRequest(
        Long optionId,
        Integer quantity,
        String message
) {
    public Order toOrder(Long userId) {
        return Order.newOrder(userId, optionId(), quantity(), message());
    }
}
