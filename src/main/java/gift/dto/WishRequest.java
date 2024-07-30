package gift.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WishRequest {
    private final Long productId;
    private final Long optionId;

    @JsonCreator
    public WishRequest(@JsonProperty("productId") Long productId, @JsonProperty("optionId") Long optionId) {
        if (productId == null) {
            throw new IllegalArgumentException("상품 id를 입력해야 합니다.");
        }
        if (optionId == null) {
            throw new IllegalArgumentException("옵션 id를 입력해야 합니다.");
        }
        this.productId = productId;
        this.optionId = optionId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }
}