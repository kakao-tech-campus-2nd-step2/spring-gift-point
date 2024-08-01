package gift.DTO.Order;

import gift.domain.Order;

public class OrderResponse {
    Long id;
    private Long optionId;
    private int quantity;
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

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }
}
