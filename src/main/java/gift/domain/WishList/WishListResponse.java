package gift.domain.WishList;

public record WishListResponse(
        Long Id,
        String name,
        int price,
        String imageUrl
) {
}
