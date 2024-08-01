package gift.order.dto;

public class OrderRequest {

    private Long memberId;
    private Long productId;
    private Long giftOptionId;
    private Integer quantity;
    private String msg;

    public OrderRequest(Long memberId, Long productId, Long giftOptionId, Integer quantity, String msg) {
        this.memberId = memberId;
        this.productId = productId;
        this.giftOptionId = giftOptionId;
        this.quantity = quantity;
        this.msg = msg;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getGiftOptionId() {
        return giftOptionId;
    }

    public String getMsg() {
        return msg;
    }

    public int getQuantity() {
        return quantity;
    }
}
