package gift.wish.dto.response;


import gift.product.dto.response.ProductResponse;
import gift.wish.entity.Wish;

public record WishResponse(
    Long id,
    ProductResponse product,
    Integer quantity
) {

    public static WishResponse from(Wish wish) {
        ProductResponse product = ProductResponse.from(wish.getProduct());
        return new WishResponse(
            wish.getId(), product, wish.getQuantity()
        );
    }

}
