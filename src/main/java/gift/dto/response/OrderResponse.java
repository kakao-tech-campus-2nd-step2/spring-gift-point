package gift.dto.response;

import gift.domain.Order;

import java.time.LocalDateTime;

public record OrderResponse(Long id, Long optionId, int quantity, LocalDateTime orderDateTime, int pointsUsed, int pointsReceived, int payment, String message) {
    public static OrderResponse from(final Order order){
        Long optionId = order.getOption().getId();
        return new OrderResponse(order.getId(), optionId, order.getQuantity(), order.getOrderDateTime(), order.getPointsUsed(), order.getPointsReceived(), order.getPayment(), order.getMessage());
    }
}
