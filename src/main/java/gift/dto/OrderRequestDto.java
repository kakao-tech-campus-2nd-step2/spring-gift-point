package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderRequestDto {
    @NotNull(message = "옵션 ID는 필수 입력 값입니다.")
    private Long optionId;
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    private int quantity;
    private String message;
    @Min(value = 0, message = "포인트는 최소 0 이상이어야 합니다.")
    private int point;

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public int getPoint() {
        return point;
    }
}
