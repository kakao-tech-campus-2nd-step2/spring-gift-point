package gift.dto.response;

import gift.entity.Category;

public record CategoryResponse(
        Long id,
        String name,
        String color,
        String imageUrl,
        String description

) {
    public static CategoryResponse fromCategory(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getImageUrl(),
                category.getDescription()
        );
    }
}
