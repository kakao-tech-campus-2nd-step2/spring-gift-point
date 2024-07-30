package gift.Entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class WishlistId implements Serializable {

    private long memberId;
    private long productId;

    protected WishlistId() {
    }

    public WishlistId(long memberId, long productId) {
        this.memberId = memberId;
        this.productId = productId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}