package gift.product.dto.product;

public record ProductResponse(
    Long id,
    String name,
    int price,
    String imageUrl,
    Long categoryId
) {

}
