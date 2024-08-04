package gift.product.application.command;

public record ProductUpdateCommand(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        Long categoryId
) {
}
