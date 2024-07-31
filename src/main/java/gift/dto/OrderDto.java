package gift.dto;

import java.time.LocalDateTime;

import gift.entity.Order;


public class OrderDto {
    private Long id;
    private Long optionId;
    private int quantity;
    private LocalDateTime orderTime;
    private String message;

    public OrderDto(Long id, Long optionId, int quantity,LocalDateTime orderTime, String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderTime = orderTime;
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

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getMessage() {
        return message;
    }

    public static OrderDto fromEntity(Order order){
        return new OrderDto(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderTime(), order.getMessage());
    }
}
