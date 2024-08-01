package gift.wish.dto.response;

import gift.user.dto.ProductDto;
import gift.wish.entity.Wish;

public record GetWishResponse(
    Long id,
    ProductDto product
) {

    public static GetWishResponse from(Wish wish) {
        var product = ProductDto.from(wish.getProduct());
        return new GetWishResponse(wish.getId(), product);
    }

}
