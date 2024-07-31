package gift.order;

import gift.product.Product;

import java.time.LocalDateTime;

public record OrderResponse (Long id, Long optionId, Long quantity, LocalDateTime orderDateTime, String message){
    public OrderResponse(Order order) {
        this(order.getId(), order.getOptionId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }
}
