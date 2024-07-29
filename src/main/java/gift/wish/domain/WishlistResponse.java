package gift.wish.domain;

import gift.product.domain.ProductDTO;
import java.time.LocalDateTime;

public record WishlistResponse(Long wishlistId, Long userId, ProductDTO productDTO, Long amount, LocalDateTime createdDate) {

}
