package gift.dto;


import java.time.LocalDateTime;

public class OrderDTO {
    private Long orderId;
    private Long optionId;
    private int quantity;
    private String message;
    private LocalDateTime orderDateTime;

    public OrderDTO() {}


    public OrderDTO(Long orderId, Long optionId, int quantity, String message, LocalDateTime orderDateTime) {
        this.orderId = orderId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }

    // Getters and setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }
}