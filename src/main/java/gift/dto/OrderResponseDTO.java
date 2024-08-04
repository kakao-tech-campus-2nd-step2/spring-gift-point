package gift.dto;

import gift.entity.Orders;
import jakarta.persistence.criteria.Order;
import java.time.LocalDateTime;
import java.util.Date;

public class OrderResponseDTO {

    private Long optionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;
    private int point;

    public OrderResponseDTO(Long optionId, int quantity, LocalDateTime orderDateTime,
        String message, int point) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.point = point;
    }

    public OrderResponseDTO(Orders order) {
        this.optionId = order.getOption().getId();
        this.point = order.getPoint();
        this.quantity = order.getQuantity();
        this.message = order.getMessage();
        this.orderDateTime = order.getOrderDateTime();
    }


    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public int getPoint() {
        return point;
    }
}
