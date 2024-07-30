package gift.order.domain;

import java.time.LocalDateTime;

public class OrderCreateResponse {
    Long id;
    Long optionId;
    Long quantity;
    LocalDateTime orderDateTime;
    String message;

    public OrderCreateResponse(Long id, Long optionId, Long quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }
}
