package gift.product.dto;

import gift.product.model.Order;

public class OrderResponseDTO {
    private final Long id;
    private final Long optionId;
    private final int quantity;
    private final String orderDateTime;
    private final String message;

    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.optionId = order.getOption().getId();
        this.quantity = order.getQuantity();
        this.orderDateTime = order.getOrderDateTime();
        String message = order.getMessage();
        if(message == null)
            message = "Please handle this order with care.";
        this.message = message;
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

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
