package gift.dto;

import gift.entity.Product;
import gift.entity.Wish;

public class WishResponse {

    private Long id;
    private Product product;

    public WishResponse(Long id, Product product) {
        this.id = id;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public static WishResponse from(Wish wish) {
        return new WishResponse(wish.getId(), wish.getProduct());
    }
}
