package gift.category.presentation.request;

import gift.category.application.command.CategoryCreateCommand;

public record CategoryCreateRequest(
        String name,
        String color,
        String description,
        String imageUrl
) {
    public CategoryCreateCommand toCommand() {
        return new CategoryCreateCommand(
                name,
                color,
                description,
                imageUrl
        );
    }
}
