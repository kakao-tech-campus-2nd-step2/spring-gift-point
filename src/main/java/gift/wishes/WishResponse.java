package gift.wishes;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WishResponse(
    Long productId,
    String name,
    int price,
    String imageUrl,
    long quantity
) {

    public static WishResponse from(Wish wish) {
        return new WishResponse(
            wish.getProduct().getId(),
            wish.getProduct().getName(),
            wish.getProduct().getPrice(),
            wish.getProduct().getImageUrl(),
            wish.getQuantity()
        );
    }
}

