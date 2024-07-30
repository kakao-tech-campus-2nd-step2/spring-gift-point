package gift.wishes.restapi.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.core.domain.product.Product;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WishResponse(
        Long productId,
        String name,
        Integer price,
        String imageUrl
) {
    public static WishResponse from(Product product) {
        return new WishResponse(
                product.id(),
                product.name(),
                product.price(),
                product.imageUrl()
        );
    }
}
