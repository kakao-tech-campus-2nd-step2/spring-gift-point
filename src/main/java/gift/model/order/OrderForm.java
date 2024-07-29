package gift.model.order;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 입력 폼")
public class OrderForm {

    @Schema(description = "주문 대상 ID", example = "1")
    private final Long targetId;

    @Schema(description = "주문할 상품 ID", example = "100")
    private final Long itemId;

    @Schema(description = "선택한 옵션 ID", example = "10")
    private final Long optionId;

    @Schema(description = "주문 수량", example = "2")
    private final Long quantity;

    @Schema(description = "주문 메시지", example = "생일 축하합니다!")
    private final String message;


    public OrderForm(Long targetId, Long itemId, Long optionId, Long quantity, String message) {
        this.targetId = targetId;
        this.itemId = itemId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getTargetId() {
        return targetId;
    }

    public Long getItemId() {
        return itemId;
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
