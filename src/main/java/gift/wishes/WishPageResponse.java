package gift.wishes;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import org.springframework.data.domain.Page;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WishPageResponse(
    List<WishResponse> contents,
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
