package gift.dto;
import gift.entity.WishList;

public class WishListDto {

    
    private long productId;
    private long userId;

    public WishListDto(long productId, long userId) {
        this.productId = productId;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public static WishListDto fromEntity(WishList wishlist) {
        return new WishListDto(wishlist.getMember().getId(), wishlist.getMember().getId());
    }
}
