package gift.order.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class OrderRequest {
    Long optionId;
    @Min(value = 1, message = "Quantity은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "Quantity는 최대 1억 미만 개까지 가능합니다.")
    Long quantity;
    String message;
    Long points;

    // Constructors
    public OrderRequest() {
    }
    public OrderRequest(Long optionId, Long quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    // Getters and setters
    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
}
