package gift.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 요청 DTO")
public class OrderRequest {

    @Schema(description = "옵션 이름", example = "1L")
    private Long optionId;
    @Schema(description = "옵션 이름", example = "10")
    private int quantity;
    @Schema(description = "옵션 이름", example = "배송 메세지")
    private String message;

    private OrderRequest() {
    }

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
