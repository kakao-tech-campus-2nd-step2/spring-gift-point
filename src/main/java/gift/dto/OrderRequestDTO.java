package gift.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class OrderRequestDTO {

    @Valid
    @NotEmpty(message = "Option Id는 필수값입니다.")
    private Long optionId;

    @Valid
    @DecimalMin(value = "1", message = "상품의 개수는 1개 이상이어야 합니다.")
    private int quantity;

    private String message;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(Long optionId, int quantity, String message) {
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
