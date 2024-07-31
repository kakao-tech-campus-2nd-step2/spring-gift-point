package gift.wish.application.dto.response;

import gift.wish.service.dto.WishPageInfo;
import java.util.List;

public record WishPageResponse(
        List<WishResponse> wishes,
        long totalElements,
        int totalPages,
        int currentPage
) {
    public static WishPageResponse from(WishPageInfo wishPageInfo) {
        List<WishResponse> wishes = wishPageInfo.wishes().stream()
                .map(WishResponse::from)
                .toList();

        return new WishPageResponse(
                wishes,
                wishPageInfo.totalElements(),
                wishPageInfo.totalPages(),
                wishPageInfo.currentPage()
        );
    }
}
