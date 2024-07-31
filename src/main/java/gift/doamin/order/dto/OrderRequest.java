package gift.doamin.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "주문 요청")
public class OrderRequest {

    @Schema(description = "주문할 상품 옵션 id")
    @NotNull
    Long optionId;

    @Schema(description = "주문할 수량")
    @Positive
    Integer quantity;

    @Schema(description = "수령인에게 보낼 메시지")
    String message;

    public OrderRequest(Long optionId, Integer quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
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
}
