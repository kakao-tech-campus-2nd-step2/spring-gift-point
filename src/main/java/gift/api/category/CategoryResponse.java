package gift.api.category;

public record CategoryResponse(
    Long id,
    String name,
    String color,
    String imageUrl,
    String description
) {
    public static CategoryResponse of(Category category) {
        return new CategoryResponse(category.getId(),
                                    category.getName(),
                                    category.getColor(),
                                    category.getImageUrl(),
                                    category.getDescription());
    }
}
