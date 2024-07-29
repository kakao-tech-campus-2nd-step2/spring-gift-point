package gift.wishes;

import java.util.List;
import org.springframework.data.domain.Page;

public record WishPageResponse(
    List<WishResponse> wishList,
    long totalElements,
    int totalPage,
    int currentPage
) {

    public static WishPageResponse from(Page<Wish> wishPage) {
        List<WishResponse> wishlist = wishPage.getContent().stream()
            .map(WishResponse::from)
            .toList();

        return new WishPageResponse(
            wishlist,
            wishPage.getTotalElements(),
            wishPage.getTotalPages(),
            wishPage.getNumber()
        );
    }
}
