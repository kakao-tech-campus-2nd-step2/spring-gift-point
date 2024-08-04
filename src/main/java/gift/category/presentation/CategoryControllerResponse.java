package gift.category.presentation;

import gift.category.application.CategoryServiceResponse;

public record CategoryControllerResponse(
        Long id,
        String name,
        String color,
        String description,
        String imageUrl
) {
    public static CategoryControllerResponse from(CategoryServiceResponse category) {
        return new CategoryControllerResponse(
                category.id(),
                category.name(),
                category.color(),
                category.description(),
                category.imageUrl()
        );
    }
}
