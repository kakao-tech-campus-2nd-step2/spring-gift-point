package gift.product.dto.product;

public record ProductInfoForWishResponse(
    Long id,
    String name,
    int price,
    String imageUrl
) {

}
