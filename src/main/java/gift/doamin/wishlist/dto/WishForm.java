package gift.doamin.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "위시리스트 등록, 수정, 삭제 요청")
public class WishForm {

    @Schema(description = "희망 상품 id")
    @NotNull
    private Long productId;

    @Schema(description = "희망 수량")
    @PositiveOrZero
    private Integer quantity;

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public boolean isZeroQuantity() {
        return quantity == null || quantity.equals(0);
    }
}
