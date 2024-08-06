package gift.controller.wish;

import gift.controller.member.WishMemberResponse;
import gift.controller.product.WishProductResponse;
import gift.domain.Product;
import gift.domain.Wish;

public class WishMapper {

    public static WishResponse toWishResponse(Wish wish) {
        WishMemberResponse wishMember = new WishMemberResponse(wish.getMember().getId(), wish.getMember().getEmail());
        Product product = wish.getProduct();
        WishProductResponse wishProduct = new WishProductResponse(product.getId(), product.getName(),
            product.getPrice(), product.getImageUrl(), product.getCategoryName());
        return new WishResponse(wish.getWishId(), wishMember, wishProduct);
    }
}