package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for creating or updating a Wish")
public class WishRequest {

    @Schema(description = "ID of the product", example = "1")
    private Long productId;

    public WishRequest() {
    }

    public WishRequest(Long product_id) {
        this.productId = product_id;
    }

    public Long getProductId() {
        return productId;
    }
}
