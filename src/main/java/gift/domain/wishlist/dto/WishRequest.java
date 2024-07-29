package gift.domain.wishlist.dto;

public class WishRequest {

    private Long memberId;

    private Long productId;

    private WishRequest() {
    }

    public WishRequest(Long memberId, Long productId) {
        this.memberId = memberId;
        this.productId = productId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }
}
