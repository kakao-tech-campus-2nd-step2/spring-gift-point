package gift.dto.order;

import java.time.LocalDateTime;

public class OrderResponse {
    private Long id;
    private Long optionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;
    private int point;

    public OrderResponse() {}

    public OrderResponse(Long id, Long optionId, int quantity, int point, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.point = point;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public OrderResponse(String message) {
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
