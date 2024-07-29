package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderRequestDto {

    @NotNull(message = "옵션은 필수입니다.")
    private Long optionId;

    @Min(value = 1, message = "최소 1개이상의 수량이 필요합니다.")
    @Max(value = 100000000, message = "최대 1억개의 수량이 가능합니다.")
    private int quantity;

    private String message;

    public OrderRequestDto() {
    }

    public OrderRequestDto(Long optionId, int quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public OrderRequestDto(Long optionId, int quantity) {
        this.optionId = optionId;
        this.quantity = quantity;
    }


    public @NotNull(message = "옵션은 필수입니다.") Long getOptionId() {
        return optionId;
    }

    public void setOptionId(
        @NotNull(message = "옵션은 필수입니다.") Long optionId) {
        this.optionId = optionId;
    }

    @Min(value = 1, message = "최소 1개이상의 수량이 필요합니다.")
    @Max(value = 100000000, message = "최대 1억개의 수량이 가능합니다.")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(
        @Min(value = 1, message = "최소 1개이상의 수량이 필요합니다.") @Max(value = 100000000, message = "최대 1억개의 수량이 가능합니다.") int quantity) {
        this.quantity = quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
