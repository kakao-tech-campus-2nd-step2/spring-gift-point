package gift.web.dto.response.wishproduct;

import gift.domain.Product;
import gift.domain.WishProduct;
import java.util.Objects;

public class ReadWishProductResponse {

    private final Long id;
    private final Long productId;
    private final String name;
    private final Integer price;
    private final Integer quantity;
    private final String imageUrl;

    public ReadWishProductResponse(Long id, Long productId, String name, Integer price,
        Integer quantity, String imageUrl) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public static ReadWishProductResponse fromEntity(WishProduct wishProduct) {
        Product product = wishProduct.getProduct();
        return new ReadWishProductResponse(wishProduct.getId(), product.getId(), product.getName(), product.getPrice(),
            wishProduct.getQuantity(), product.getImageUrl().toString());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReadWishProductResponse that = (ReadWishProductResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(productId,
            that.productId) && Objects.equals(name, that.name) && Objects.equals(
            price, that.price) && Objects.equals(quantity, that.quantity)
            && Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, name, price, quantity, imageUrl);
    }
}
