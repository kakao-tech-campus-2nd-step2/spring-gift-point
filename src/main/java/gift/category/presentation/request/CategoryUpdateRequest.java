package gift.category.presentation.request;

import gift.category.application.command.CategoryUpdateCommand;

public record CategoryUpdateRequest(
        String name,
        String color,
        String description,
        String imageUrl
) {
    public CategoryUpdateCommand toCommand(Long categoryId) {
        return new CategoryUpdateCommand(
                categoryId,
                name,
                color,
                description,
                imageUrl
        );
    }
}
