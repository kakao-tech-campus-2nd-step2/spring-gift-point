package gift.domain.Category;

public record CategoryRequest(
        String name,
        String description,
        String color,
        String imageUrl
) {
}
