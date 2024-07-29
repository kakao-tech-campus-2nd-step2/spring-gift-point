package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public class OrderDTO {
    @Schema(description = "상품 id", nullable = false, example = "1")
    private Long productId;
    @Schema(description = "옵션 id", nullable = false, example = "1")
    private Long optionId;
    @Schema(description = "주문 수량", nullable = false, example = "5")
    private int quantity;
    @Schema(description = "주문 메시지", nullable = false, example = "주문 메시지 입니다")
    private String message;

    public OrderDTO() {
    }

    public OrderDTO(Long productId, Long optionId, int quantity, String message) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getProductId() {
        return productId;
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
