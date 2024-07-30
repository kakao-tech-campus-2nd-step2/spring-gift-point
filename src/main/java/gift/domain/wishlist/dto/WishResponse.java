package gift.domain.wishlist.dto;

import java.time.LocalDateTime;

public record WishResponse(Long id, Long memberId, Long productId, LocalDateTime createdDate) {

}
