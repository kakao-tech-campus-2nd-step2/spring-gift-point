package gift.dto;

import java.time.LocalDateTime;

public class OrderResponseDto {
    private Long id;
    private Long productOptionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;

    public OrderResponseDto(Long id, Long productOptionId, int quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.productOptionId = productOptionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
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
}
