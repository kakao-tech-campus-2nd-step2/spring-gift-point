package gift.dto;

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
