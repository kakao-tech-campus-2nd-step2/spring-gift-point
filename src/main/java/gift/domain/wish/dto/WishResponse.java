package gift.domain.wish.dto;

public record WishResponse(
    Long id,
    Long productId,
    String productName,
    int price,
    String imageUrl
) {

}
