package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "선물 주문 요청 DTO")
public class GiftOrderRequestDto {
    @Schema(description = "선택한 옵션 id")
    private Long optionId;
    @Schema(description = "선택한 상품 개수")
    private Long quantity;
    @Schema(description = "선물과 함께 보낼 메세지")
    private String message;

    public GiftOrderRequestDto(Long optionId, Long quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
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
}
