package gift.wish.model;

public class WishRequest {
    private Long productId;

    // Constructor 추가
    public WishRequest() {}

    public WishRequest(Long productId) {
        this.productId = productId;
    }

    // Getter 및 Setter 추가
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
