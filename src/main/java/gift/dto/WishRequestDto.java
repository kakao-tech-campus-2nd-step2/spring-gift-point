package gift.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class WishRequestDto {

    @NotNull(message = "Product ID는 빈 칸일 수 없습니다.")
    private final Long productId;

    @JsonCreator
    public WishRequestDto(@JsonProperty("productId") Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
