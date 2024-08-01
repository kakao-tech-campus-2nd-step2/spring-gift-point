package gift.doamin.wishlist.dto;

import gift.doamin.wishlist.entity.Wish;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시리스트 항목 정보")
public class WishResponse {

    @Schema(description = "위시 id")
    private Long id;

    @Schema(description = "희망 상품 옵션 id")
    private Long optionId;

    @Schema(description = "희망 수량")
    private Integer quantity;

    public WishResponse(Wish wish) {
        this.id = wish.getId();
        this.optionId = wish.getOption().getId();
        this.quantity = wish.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
