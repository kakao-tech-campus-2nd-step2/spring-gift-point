package gift.wish.service.dto;

import gift.wish.domain.Wish;
import java.util.List;
import org.springframework.data.domain.Page;

public record WishPageInfo(
        List<WishInfo> wishes,
        long totalElements,
        int totalPages,
        int currentPage
) {
    public static WishPageInfo from(Page<Wish> wishPage) {
        List<WishInfo> wishes = wishPage.getContent().stream()
                .map(WishInfo::from)
                .toList();

        return new WishPageInfo(
                wishes,
                wishPage.getTotalElements(),
                wishPage.getTotalPages(),
                wishPage.getNumber()
        );
    }
}
