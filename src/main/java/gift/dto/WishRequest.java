package gift.dto;

public class WishRequest {
    private Long productId;

    public WishRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
