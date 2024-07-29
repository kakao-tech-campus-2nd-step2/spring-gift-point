package gift.dto.request;

import jakarta.validation.constraints.NotNull;

public class WishCreateRequest {

    @NotNull
    private Long productId;

    public WishCreateRequest() {
    }

    public WishCreateRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

}
