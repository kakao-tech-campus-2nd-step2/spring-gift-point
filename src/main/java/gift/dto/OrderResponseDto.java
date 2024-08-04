package gift.dto;

import gift.model.Order;

import java.time.LocalDateTime;

public class OrderResponseDto {
    private Long id;
    private Long optionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;
    private int point;

    public OrderResponseDto(Long id, Long optionId, int quantity, LocalDateTime orderDateTime, String message, int point) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.point = point;
    }

    public OrderResponseDto(Order order) {
        this(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage(), order.getPoint());
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
