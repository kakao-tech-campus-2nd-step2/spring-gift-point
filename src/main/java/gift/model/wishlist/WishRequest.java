package gift.model.wishlist;

import gift.model.product.Product;
import gift.model.user.User;

public record WishRequest(Long productId, int quantity) {
    public WishList toEntity(User user, Product product) {
        return new WishList(user, product, quantity);
    }

}
