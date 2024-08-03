package gift.wish.application.dto.response;

import gift.wish.service.dto.WishPageInfo;
import java.util.List;
import org.springframework.data.domain.Pageable;

public record WishPageResponse(
        List<WishResponse> contents,
        Pageable pageable,
        Integer totalPages,
        Long totalElements,
        Boolean last,
        Integer number,
        Integer size,
        Integer numberOfElements,
        Boolean first,
        Boolean empty
) {
    public static WishPageResponse from(WishPageInfo wishPageInfo) {
        List<WishResponse> wishes = wishPageInfo.wishes().stream()
                .map(WishResponse::from)
                .toList();

        return new WishPageResponse(
                wishes,
                wishPageInfo.pageable(),
                wishPageInfo.totalPages(),
                wishPageInfo.totalElements(),
                wishPageInfo.last(),
                wishPageInfo.number(),
                wishPageInfo.size(),
                wishPageInfo.numberOfElements(),
                wishPageInfo.first(),
                wishPageInfo.empty()
        );
    }
}
