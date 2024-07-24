package gift.dto;

public class WishListRequest {
    private Long productId;
    private Integer productCount;

    public WishListRequest() {
    }

    public WishListRequest(Long productId, Integer productCount) {
        this.productId = productId;
        this.productCount = productCount;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getProductCount() {
        return productCount;
    }
}
