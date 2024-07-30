package gift.product.domain;

public class AddWishListRequest {
    private Long userId;
    private Long wishlistId;
    private Long productId;
    private Long optionId;
    private Long quantity;

    public Long getUserId() {
        return userId;
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public AddWishListRequest(Long userId, Long wishlistId, Long productId, Long optionId, Long quantity) {
        this.userId = userId;
        this.wishlistId = wishlistId;
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
    }
}
