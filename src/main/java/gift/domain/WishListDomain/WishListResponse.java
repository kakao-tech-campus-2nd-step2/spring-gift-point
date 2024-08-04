package gift.domain.WishListDomain;

public record WishListResponse(
        Long Id,
        String name,
        int price,
        String imageUrl
) {
}
