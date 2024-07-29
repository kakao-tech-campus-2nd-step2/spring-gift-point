package gift.dto;

import java.time.LocalDateTime;

public class OrderResponseDto {
    private long id;
    private long optionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;

    public OrderResponseDto(long id, long optionId, int quantity, LocalDateTime orderDateTime,
        String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public long getOptionId() {
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
}
