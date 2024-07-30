package gift.dto;

import gift.model.Order;

import java.time.LocalDateTime;

public class OrderResponse {
    private Long id;
    private Long optionId;
    private int quantity;
    private String message;
    private LocalDateTime orderDateTime;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.optionId = order.getProductOption().getId();
        this.quantity = order.getQuantity();
        this.message = order.getMessage();
        this.orderDateTime = order.getOrderDateTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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