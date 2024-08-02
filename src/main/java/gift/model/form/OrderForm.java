package gift.model.form;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 옵션 입력 폼 객체")
public class OrderForm {

    @Schema(description = "주문 할 상품 id", example = "1")
    private final Long productId;
    @Schema(description = "주문 할 옵션 id", example = "1")
    private final Long optionId;
    @Schema(description = "주문 할 옵션 수량", example = "2")
    private final Long quantity;
    @Schema(description = "배송 특이 사항", example = "부재 시 경비실에 맡겨주세요.")
    private final String message;
    @Schema(description = "주문 시 사용할 포인트", example = "2000")
    private final Long point;

    public OrderForm(Long productId, Long optionId, Long quantity, String message, Long point) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.point = point;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getPoint() {
        return point;
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
