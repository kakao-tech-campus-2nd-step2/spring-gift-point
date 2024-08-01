package gift.dto;

import jakarta.validation.constraints.NotNull;

public class WishRequestDto {

    @NotNull(message = "상품 ID는 필수 입니다.")
    private Long productId;

    public WishRequestDto() {
    }

    public WishRequestDto(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}