package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {

    @NotNull(message = "옵션 ID는 필수입니다")
    private Long optionId;

    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다")
    private int quantity;

    @NotBlank(message = "메시지는 필수입니다")
    private String message;

    public OrderRequest() {}

    public OrderRequest(Long optionId, int quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
