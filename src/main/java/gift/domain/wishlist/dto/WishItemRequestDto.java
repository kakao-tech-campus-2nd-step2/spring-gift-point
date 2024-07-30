package gift.domain.wishlist.dto;

import gift.domain.product.entity.Product;
import gift.domain.user.entity.User;
import gift.domain.wishlist.entity.WishItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "위시리스트 항목 요청 정보")
public record WishItemRequestDto(

    @NotNull(message = "상품 정보를 입력해주세요.")
    @Schema(description = "상품 ID")
    Long productId
) {
    public WishItem toWishItem(User user, Product product) {
        return new WishItem(null, user, product);
    }
}
