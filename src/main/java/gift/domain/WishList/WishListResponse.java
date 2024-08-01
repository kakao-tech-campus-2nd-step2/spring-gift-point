package gift.domain.WishList;

import gift.domain.Menu.Menu;

public record WishListResponse(
        Long Id,
        Menu menu
) {
}
