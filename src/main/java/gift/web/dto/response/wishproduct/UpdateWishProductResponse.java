package gift.web.dto.response.wishproduct;

import gift.domain.Product;
import gift.domain.WishProduct;

public class UpdateWishProductResponse {

    private final Long id;
    private final Long productId;
    private final String name;
    private final Integer price;
    private final Integer quantity;
    private final String imageUrl;

    public UpdateWishProductResponse(Long id, Long productId, String name, Integer price, Integer quantity, String imageUrl) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public static UpdateWishProductResponse fromEntity(WishProduct wishProduct) {
        Product product = wishProduct.getProduct();
        return new UpdateWishProductResponse(wishProduct.getId(), product.getId(), product.getName(),
            product.getPrice(), wishProduct.getQuantity(), product.getImageUrl().toString());
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
