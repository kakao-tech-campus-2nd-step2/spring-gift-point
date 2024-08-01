package gift.wish.service.dto;

import gift.wish.domain.Wish;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public record WishPageInfo(
        List<WishInfo> wishes,
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
    public static WishPageInfo from(Page<Wish> wishPage) {
        List<WishInfo> wishes = wishPage.getContent().stream()
                .map(WishInfo::from)
                .toList();

        return new WishPageInfo(
                wishes,
                wishPage.getPageable(),
                wishPage.getTotalPages(),
                wishPage.getTotalElements(),
                wishPage.isLast(),
                wishPage.getNumber(),
                wishPage.getSize(),
                wishPage.getNumberOfElements(),
                wishPage.isFirst(),
                wishPage.isEmpty()
        );
    }
}
