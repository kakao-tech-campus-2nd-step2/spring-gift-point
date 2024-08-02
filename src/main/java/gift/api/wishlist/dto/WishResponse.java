package gift.api.wishlist.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.api.wishlist.domain.Wish;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WishResponse(
    Long id,
    Integer quantity
) {
    public static WishResponse of(Wish wish) {
        return new WishResponse(wish.getId(),
                                wish.getQuantity());
    }
}
