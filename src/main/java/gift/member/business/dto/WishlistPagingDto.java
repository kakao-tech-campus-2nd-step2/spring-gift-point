package gift.member.business.dto;

import gift.member.persistence.entity.Wishlist;
import java.util.List;
import org.springframework.data.domain.Page;

public record WishlistPagingDto(
    boolean hasNext,
    List<WishListDto> wishlistList
) {

    public static WishlistPagingDto from(Page<Wishlist> wishlists) {
        return new WishlistPagingDto(
            wishlists.hasNext(),
            WishListDto.from(wishlists.getContent())
        );
    }
}
