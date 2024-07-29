package gift.DTO.Order;

import gift.domain.Order;

import java.util.Date;

public class OrderResponse {
    Long id;
    private Long optionId;
    private Long quantity;
    private String message;
    private String orderDateTime;

    public OrderResponse() {
    }

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.optionId = order.getOptionId();
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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }
}
