package gift.dto.request;

import jakarta.validation.constraints.NotNull;

public class WishlistRequest {

    @NotNull(message = "Product ID를 입력하세요")
    private Long productId;

    public WishlistRequest() {

    }

    public WishlistRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }


}
