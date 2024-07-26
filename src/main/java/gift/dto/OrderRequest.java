package gift.dto;

import java.time.LocalDateTime;

public class OrderRequest {
    private final Long productId;
    private final Long optionId;
    private final int quantity;
    private final String message;
    private final LocalDateTime orderTime;

    public OrderRequest(Long productId, Long optionId, int quantity, String message, LocalDateTime orderTime) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderTime = orderTime;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }
}
