package gift.dto.response;

import gift.domain.Order;

import java.time.LocalDateTime;

public record OrderResponse(Long id, Long optionId, int quantity, LocalDateTime orderDateTime, String message) {
    public OrderResponse(Order savedOrder) {
        this(savedOrder.getId(), savedOrder.getOption().getId(), savedOrder.getQuantity(), savedOrder.getOrderDateTime(), savedOrder.getMessage());
    }
}