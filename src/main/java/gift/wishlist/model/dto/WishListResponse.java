package gift.wishlist.model.dto;

public class WishListResponse {
    private Long wishId;
    private Long productId;
    private String productName;
    private int productPrice;
    private String productImageUrl;
    private int quantity;

    public WishListResponse(Long wishId, Long productId, String productName, int productPrice, String productImageUrl,
                            int quantity) {
        this.wishId = wishId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    public Long getWishId() {
        return wishId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}