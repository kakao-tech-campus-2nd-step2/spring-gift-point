package gift.dto;

import gift.domain.Order;

import java.time.LocalDate;

public class OrderResponse {
    private final Long id;
    private final Long product_id;
    private final Long option_id;
    private final int quantity;
    private final LocalDate orderDateTime;
    private final String message;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.product_id = order.getProduct().getId();
        this.option_id = order.getOption().getId();
        this.quantity = order.getQuantity();
        this.orderDateTime = order.getOrdered_at();
        this.message = order.getMessage();
    }

    public Long getOption_id() {
        return option_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return id;
    }
    public Long getProduct_id() {
        return product_id;
    }
}
