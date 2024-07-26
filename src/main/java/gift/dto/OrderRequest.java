package gift.dto;

import java.time.LocalDateTime;

public class OrderRequest {
    private Long optionId;
    private int quantity;
    private String message;
    private LocalDateTime orderTime;

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }
}
