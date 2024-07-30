package gift.dto;

import jakarta.validation.constraints.NotNull;

public class WishRequest {
    @NotNull
    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
