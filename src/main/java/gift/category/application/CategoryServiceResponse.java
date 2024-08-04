package gift.category.application;

import gift.category.domain.Category;

public record CategoryServiceResponse(
        Long id,
        String name,
        String color,
        String description,
        String imageUrl
) {
    public static CategoryServiceResponse from(Category category) {
        return new CategoryServiceResponse(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getDescription(),
                category.getImageUrl()
        );
    }
}
