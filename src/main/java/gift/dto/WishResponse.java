package gift.dto;

import gift.entity.Product;
import gift.entity.Wish;

public class WishResponse {

    private Long id;
    private Long productId;
    private String productName;

    public WishResponse(Long id, Product product) {
        this.id = id;
        this.productId = product.getId();
        this.productName = product.getName();
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public static WishResponse from(Wish wish) {
        return new WishResponse(wish.getId(), wish.getProduct());
    }
}
