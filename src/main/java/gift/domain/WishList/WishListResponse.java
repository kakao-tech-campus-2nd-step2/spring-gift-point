package gift.domain.WishList;

import gift.domain.Menu.Menu;

public record WishListResponse(
        Long Id,
        String name,
        int price,
        String imageUrl
) {
}
