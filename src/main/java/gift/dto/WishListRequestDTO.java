package gift.dto;

public class WishListRequestDTO {
    private Long productId;

    public WishListRequestDTO() {}

    public WishListRequestDTO(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
