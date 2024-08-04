package gift.product.dto;

public record GetProductResponse(
    long productId,
    String name,
    int price,
    String imageUrl,
    boolean isWish
) {

}
