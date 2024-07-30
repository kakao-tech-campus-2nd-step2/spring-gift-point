package gift.domain.wish.dto;

import java.util.List;

public record WishPageResponse(
    boolean hasNext,
    List<WishResponse> wishes
) {

}
