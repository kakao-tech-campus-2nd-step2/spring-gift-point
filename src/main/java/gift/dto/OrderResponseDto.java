package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class OrderResponseDto {
    private Long id;
    private Long productOptionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;
    private int totalPrice;
    private int pointUsed;
    private int remainPoint;

    public OrderResponseDto(Long id, @JsonProperty("OptionId") Long productOptionId, int quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.productOptionId = productOptionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public OrderResponseDto(Long id, @JsonProperty("OptionId") Long productOptionId, int quantity, LocalDateTime orderDateTime, String message, int totalPrice, int pointUsed, int remainPoint) {
        this.id = id;
        this.productOptionId = productOptionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.totalPrice = totalPrice;
        this.pointUsed = pointUsed;
        this.remainPoint = remainPoint;
    }

    public Long getId() {
        return id;
    }

    public Long getProductOptionId() {
        return productOptionId;
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

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPointUsed() {
        return pointUsed;
    }

    public int getRemainPoint() {
        return remainPoint;
    }
}
