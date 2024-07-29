package gift.wishlist;

import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.wishlist.dto.WishListReqDto;
import gift.wishlist.entity.WishList;

public class WishListFixture {

    public static WishList createWishList(Member member, Product product, Integer quantity) {
        return new WishList(member, product, quantity);
    }

    public static WishListReqDto createWishListReqDto(Long productId, Integer quantity) {
        return new WishListReqDto(productId, quantity);
    }
}
