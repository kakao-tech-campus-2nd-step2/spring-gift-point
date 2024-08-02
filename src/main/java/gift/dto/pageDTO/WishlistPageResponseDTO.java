package gift.dto.pageDTO;

import gift.model.Wishlist;
import org.springframework.data.domain.Page;

public record WishlistPageResponseDTO(
    Page<Wishlist> wishlists
) {

}
