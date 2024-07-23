package gift.dto;

import jakarta.validation.constraints.NotNull;

public class WishDto {
    @NotNull(message = "상품 id 입력은 필수 입니다.")
    private Long productId;

    public WishDto() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
