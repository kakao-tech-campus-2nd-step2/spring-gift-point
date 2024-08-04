package gift.dto.response;

import gift.domain.Order;

import java.time.LocalDateTime;

public record OrderPaginationResponse(Long id, Long optionId, int quantity, LocalDateTime orderDateTime, String message) {
    public static OrderPaginationResponse from(final Order order){
        Long optionId = order.getOption().getId();
        return new OrderPaginationResponse(order.getId(), optionId, order.getQuantity(), order.getOrderDateTime(),order.getMessage());
    }
}
