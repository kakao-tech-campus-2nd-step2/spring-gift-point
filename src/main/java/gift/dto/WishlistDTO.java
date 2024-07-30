package gift.dto;

public class WishlistDTO {
    private String userId;
    private Long productId;

    public WishlistDTO(String userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }
}