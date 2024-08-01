package gift.domain.Menu;

public record MenuUpdateRequest(
        String name,
        int price,
        String imageUrl,
        Long categoryId
) {
}
