package gift.domain.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OrderAddRequestDto {

    @NotNull
    private Long optionId;

    @NotNull
    private Integer quantity;

    @Size(max = 200, message = "메시지는 200자를 초과할 수 없습니다.")
    private String message;

    private Integer point;

    public OrderAddRequestDto() {
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public Integer getPoint() {
        return point;
    }
}
