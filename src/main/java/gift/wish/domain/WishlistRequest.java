package gift.wish.domain;

public class WishlistRequest {
    Long userId;
    Long productId;
    Long amount;

    public WishlistRequest(Long userId, Long productId, Long amount) {
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
