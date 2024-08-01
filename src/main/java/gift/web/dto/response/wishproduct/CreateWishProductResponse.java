package gift.web.dto.response.wishproduct;

import gift.domain.WishProduct;

public class CreateWishProductResponse {

    private final Long id;

    private final Integer quantity;

    public CreateWishProductResponse(Long id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public static CreateWishProductResponse fromEntity(WishProduct wishProduct) {
        return new CreateWishProductResponse(wishProduct.getId(), wishProduct.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
