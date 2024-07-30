package gift.api.category.dto;

import gift.api.category.domain.Category;

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
