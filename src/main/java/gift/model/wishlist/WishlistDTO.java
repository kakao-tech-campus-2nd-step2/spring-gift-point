package gift.model.wishlist;

import gift.validation.constraint.ProductIdConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class WishlistDTO {

    @ProductIdConstraint
    @NotNull
    @Schema(description = "상품 id", nullable = false, example = "1")
    private Long productId;

    public WishlistDTO() {
    }

    public WishlistDTO(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
