package gift.dto;

import gift.domain.Order;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;

public class OrderResponse {
    private Long id;
    private Long product_id;
    private Long option_id;
    private int quantity;
    private LocalDate orderDateTime;
    private String message;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.product_id = order.getProduct().getId();
        this.option_id = order.getOption().getId();
        this.quantity = order.getQuantity();
        this.orderDateTime = order.getOrdered_at();
        this.message = order.getMessage();
    }

    public Long getOptionId() {
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
}
