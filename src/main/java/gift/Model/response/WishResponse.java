package gift.Model.response;

public record WishResponse(Long productId, String name, int price, String imageUrl, Long wishId) {
}
