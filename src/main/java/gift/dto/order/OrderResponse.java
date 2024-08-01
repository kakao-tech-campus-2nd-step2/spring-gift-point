package gift.dto.order;

import java.time.LocalDateTime;

public class OrderResponse {
    private final Long id;
    private final Long optionId;
    private final Integer quantity;
    private final LocalDateTime orderDateTime;
    private final String message;

    public OrderResponse(Long id, Long optionId, Integer quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
