package gift.wishes.service;

public record WishDto(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        Long quantity
) {
}
