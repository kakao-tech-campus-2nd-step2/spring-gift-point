package gift.wish.domain;

import gift.product.domain.ProductDTO;

public record WishlistResponse(Long wishlistId, Long userId, ProductDTO productDTO, Long amount) {

}
