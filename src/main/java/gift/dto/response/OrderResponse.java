package gift.dto.response;

import java.time.LocalDateTime;

public class OrderResponse {

    private Long id;
    private Long optionId;
    private Long quantity;
    private LocalDateTime orderDateTime;
    private String message;

    public OrderResponse(Long id, Long optionId, Long quantity, LocalDateTime orderDateTime, String message) {
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

    public Long getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

}
