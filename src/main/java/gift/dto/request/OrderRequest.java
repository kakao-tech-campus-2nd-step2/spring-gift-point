package gift.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderRequest {

    @NotNull(message = "옵션 ID를 입력하세요.")
    private Long optionId;

    @NotNull(message = "수량을 입력하세요")
    @Positive(message = "수량은 양의 정수여야 합니다.")
    private Integer quantity;

    @NotBlank(message = "메시지를 입력하세요.")
    private String message;

    @NotNull(message = "수령 멤버를 선택하세요.")
    private Long receiveMemberId;

    public OrderRequest(Long optionId, int quantity, String message, Long receiveMemberId) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.receiveMemberId = receiveMemberId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getReceiveMemberId() {
        return receiveMemberId;
    }

    public String getMessage() {
        return message;
    }
}
