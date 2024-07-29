package gift.dto;

import jakarta.validation.constraints.NotNull;

public class WishlistRequestDto {

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;


    public WishlistRequestDto(Long productId) {
        this.productId = productId;
    }

    public WishlistRequestDto() {}

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}


