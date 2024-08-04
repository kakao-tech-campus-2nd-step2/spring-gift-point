package gift.dto;

import gift.model.Order;

import java.time.LocalDateTime;

public class OrderResponseDto {
    private Long id;
    private Long optionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;
    private int points;

    public OrderResponseDto(Long id, Long optionId, int quantity, LocalDateTime orderDateTime, String message, int points) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.points = points;
    }

    public OrderResponseDto(Order order) {
        this(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage(), order.getPoints());
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

    public int getPoints() {
        return points;
    }
}
