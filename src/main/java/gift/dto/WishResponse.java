package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.entity.Product;
import gift.entity.Wish;

public class WishResponse {

    private Long id;

    @JsonProperty("product")
    private WishProductResponse wishProductResponse;

    public WishResponse(Long id, Product product) {
        this.id = id;
        this.wishProductResponse = WishProductResponse.from(product);
    }

    public Long getId() {
        return id;
    }

    public WishProductResponse getWishProductResponse() {
        return wishProductResponse;
    }

    public static WishResponse from(Wish wish) {
        return new WishResponse(wish.getId(), wish.getProduct());
    }
}
