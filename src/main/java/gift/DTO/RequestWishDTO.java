package gift.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "찜 요청 DTO")
public class RequestWishDTO {
    @NotNull
    @Min(value = 1, message = "상품Id값은 최소 1이상입니다")
    @Schema(description = "상품 Id")
    private Long productId;

    public RequestWishDTO() {
    }

    public RequestWishDTO(Long productId, int count) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

}
