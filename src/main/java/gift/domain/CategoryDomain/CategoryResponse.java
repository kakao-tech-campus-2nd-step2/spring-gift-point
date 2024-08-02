package gift.domain.CategoryDomain;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        String color,
        String imageUrl
) {
}
