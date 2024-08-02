package gift.domain.CategoryDomain;

public record CategoryRequest(
        String name,
        String description,
        String color,
        String imageUrl
) {
}
