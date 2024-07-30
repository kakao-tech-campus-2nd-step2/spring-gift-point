package gift.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class OrderResponseDTO {
    private Long optionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;

    public OrderResponseDTO( Long optionId, int quantity, LocalDateTime orderDateTime,
        String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
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
}
