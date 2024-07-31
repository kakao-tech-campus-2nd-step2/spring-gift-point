package gift.entity.order;

import io.swagger.v3.oas.annotations.media.Schema;

public class OrderDTO {
    @Schema(description = "상품 id", nullable = false, example = "1")
    private Long product_id;
    @Schema(description = "옵션 id", nullable = false, example = "1")
    private Long option_id;
    @Schema(description = "주문 수량", nullable = false, example = "5")
    private int quantity;
    @Schema(description = "주문 메시지", nullable = false, example = "주문 메시지 입니다")
    private String message;

    public OrderDTO() {
    }

    public OrderDTO(Long productId, Long optionId, int quantity, String message) {
        this.product_id = productId;
        this.option_id = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Long getOption_id() {
        return option_id;
    }

    public void setOption_id(Long option_id) {
        this.option_id = option_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
