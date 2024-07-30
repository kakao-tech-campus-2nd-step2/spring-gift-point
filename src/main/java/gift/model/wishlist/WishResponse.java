package gift.model.wishlist;

import gift.model.product.Product;

public record WishResponse(Long id, Long productId, String Name, int count) {
    public static WishResponse from(WishList wishList, Product product) {
        return new WishResponse(wishList.getId(), product.getId(), product.getName(),
            wishList.getQuantity());
    }

}
