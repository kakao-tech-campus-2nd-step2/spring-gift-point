package gift.domain.Category;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        String color,
        String imageUrl
) {
}
