package gift.dto.wish;

public class WishListResponse {
    ProductByWish product;
    private Long id;
    private int quantity;

    public WishListResponse(Long wishId, Long productId, String productName, int productPrice, String productImageUrl,
                            int quantity) {
        this.id = wishId;
        this.product = new ProductByWish(productId, productName, productPrice, productImageUrl);
        this.quantity = quantity;
    }

    public Long getWishId() {
        return id;
    }

    public ProductByWish getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public record ProductByWish(Long id,
                                String name,
                                int price,
                                String imageUrl) {
    }
}