package gift.wishes.restapi.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.wishes.service.WishDto;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WishResponse(
        Long productId,
        String name,
        Integer price,
        String imageUrl,
        Long quantity
) {
    public static WishResponse from(WishDto wish) {
        return new WishResponse(
                wish.id(),
                wish.name(),
                wish.price(),
                wish.imageUrl(),
                wish.quantity()
        );
    }
}
