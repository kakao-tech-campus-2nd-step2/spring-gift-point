package gift.wish.dto;

public class WishCreateRequest {
    private Long productId;

    // Constructor 추가
    public WishCreateRequest() {}

    public WishCreateRequest(Long productId) {
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
