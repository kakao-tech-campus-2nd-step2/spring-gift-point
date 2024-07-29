package gift.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

public class WishRequestDto {

    @NotEmpty(message = "상품 ID는 필수 입니다.")
    private Long productId;

    @PositiveOrZero(message = "수량은 0개 이상이어야 합니다.")
    private int quantity;

    public WishRequestDto(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
