package gift.model.wishlist;

import gift.model.product.Product;

public record WishResponse(Long id, Long productId, String Name, int price, String imageUrl) {
    public static WishResponse from(WishList wishList, Product product) {
        return new WishResponse(wishList.getId(), product.getId(), product.getName(),
            product.getPrice(), product.getImageUrl());
    }

}
