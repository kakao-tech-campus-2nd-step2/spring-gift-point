package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderRequestDto {

    @NotNull(message = "옵션은 필수 입력 사항입니다.")
    private final Long optionId;

    @Min(value = 1, message = "구매 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 999999999, message = "구매 수량은 1억 미만이어야 합니다.")
    @NotNull
    private final Long quantity;

    @NotNull(message = "주문 메시지는 필수 입력사항입니다.")
    private final String message;

    @NotNull(message = "포인트는 필수 입력사항입니다. 포인트를 사용하지 않을 경우 0으로 입력해주세요.")
    private final Integer point;

    public OrderRequestDto(Long optionId, Long quantity, String message, Integer point) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.point = point;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public Integer getPoint() {
        return point;
    }
}