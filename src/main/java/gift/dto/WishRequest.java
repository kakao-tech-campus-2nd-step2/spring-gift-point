package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WishRequest {

    @JsonProperty("product_id")
    private Long productId;

    public WishRequest() {
    }

    public WishRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

}
