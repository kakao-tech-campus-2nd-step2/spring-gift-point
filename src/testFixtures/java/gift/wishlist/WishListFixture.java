package gift.wishlist;

import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.wishlist.dto.WishListReqDto;
import gift.wishlist.entity.WishList;

public class WishListFixture {

    public static WishList createWishList(Member member, Product product) {
        return new WishList(member, product);
    }

    public static WishListReqDto createWishListReqDto(Long productId) {
        return new WishListReqDto(productId);
    }
}
