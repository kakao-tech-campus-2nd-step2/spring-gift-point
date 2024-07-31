package gift.product.dto.product;

public record ProductInfoResponse(
    Long id,
    String name,
    int price,
    String imageUrl
) {

}
