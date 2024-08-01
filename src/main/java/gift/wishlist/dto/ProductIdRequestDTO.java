package gift.wishlist.dto;

public class ProductIdRequestDTO {
    private long productId;

    public ProductIdRequestDTO() {
    }

    public ProductIdRequestDTO(long productId) {
        this.productId = productId;
    }

    public long getProductId() {
        return productId;
    }
}
