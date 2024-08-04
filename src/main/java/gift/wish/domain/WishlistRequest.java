package gift.wish.domain;

public class WishlistRequest {
    Long userId;
    Long productId;

    public WishlistRequest(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
