package gift.dto.response;

import gift.domain.Order;

import java.time.LocalDateTime;

public class OrderResponse {

    private Long id;
    private Long optionId;
    private Integer quantity;
    private LocalDateTime orderDateTime;
    private String message;
    private Integer price;
    private boolean success;

    public OrderResponse(Long id, Long optionId, Integer quantity, LocalDateTime orderDateTime, String message, Integer price, boolean success) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.price = price;
        this.success = success;
    }

    public static OrderResponse fromOrder(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage(),
                order.getPrice(),
                order.isSuccess()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public Integer getPrice() {
        return price;
    }

    public boolean isSuccess() {
        return success;
    }

}
