package gift.dto.response;

import java.time.LocalDateTime;

public class OrderResponse {

    private Long id;
    private Long optionId;
    private Long quantity;
    private LocalDateTime orderDateTime;
    private String message;
    private Integer point;

    public OrderResponse(Long id, Long optionId, Long quantity, LocalDateTime orderDateTime, String message, Integer point) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public Integer getPoint() {
        return point;
    }

}
