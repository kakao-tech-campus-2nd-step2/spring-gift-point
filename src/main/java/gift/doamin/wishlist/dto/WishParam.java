package gift.doamin.wishlist.dto;

import gift.doamin.wishlist.entity.Wish;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시리스트 항목 정보")
public class WishParam {

    @Schema(description = "희망 상품 id")
    private Long productId;

    @Schema(description = "희망 수량")
    private Integer quantity;

    public WishParam(Wish wish) {
        this.productId = wish.getProduct().getId();
        this.quantity = wish.getQuantity();
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
