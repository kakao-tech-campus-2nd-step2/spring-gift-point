package gift.category.application.command;

public record CategoryUpdateCommand(
        Long categoryId,
        String name,
        String color,
        String description,
        String imageUrl
) {
}
