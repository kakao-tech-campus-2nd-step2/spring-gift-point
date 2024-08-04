package gift.dto.order;

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
    @Schema(description = "주문 포인트", nullable = false, example = "1")
    private int point;

    public OrderDTO() {
    }

    public OrderDTO(Long productId, Long optionId, int quantity, String message) {
        this.product_id = productId;
        this.option_id = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public OrderDTO(Long product_id, Long option_id, int quantity, String message, int point) {
        this.product_id = product_id;
        this.option_id = option_id;
        this.quantity = quantity;
        this.message = message;
        this.point = point;
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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
