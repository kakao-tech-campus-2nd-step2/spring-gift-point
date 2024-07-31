package gift.users.wishlist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "위시리스트 DTO")
public class WishListDTO {

    private long userId;
    @Schema(description = "위시리스트에 담을 상품 아이디", example = "1")
    @NotNull(message = "상품 아이디를 입력하지 않았습니다.")
    @Min(value = 1, message = "상품 아이디는 1 이하일 수 없습니다.")
    private long productId;
    @Schema(description = "위시리스트에 담을 상품 수량", example = "5")
    @NotNull(message = "상품 수량을 입력하지 않았습니다.")
    @Min(value = 1, message = "상품 수량은 1 이하일 수 없습니다.")
    private int quantity;
    @Schema(description = "위시리스트에 담을 상품의 옵션 아이디", example = "1")
    @NotNull(message = "옵션 아이디를 입력하지 않았습니다.")
    @Min(value = 1, message = "옵션 아이디는 1 이하일 수 없습니다.")
    private long optionId;

    public WishListDTO() {
    }

    public WishListDTO(long userId, long productId, int quantity, long optionId) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.optionId = optionId;
    }

    public long getProductId() {
        return productId;
    }

    public long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public static WishListDTO fromWishList(WishList wishList) {
        return new WishListDTO(wishList.getUser().getId(), wishList.getProduct().getId(),
            wishList.getQuantity(), wishList.getOption().getId());
    }
}
