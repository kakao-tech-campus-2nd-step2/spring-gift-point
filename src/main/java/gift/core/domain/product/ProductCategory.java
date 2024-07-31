package gift.core.domain.product;

public record ProductCategory(
        Long id,
        String name,
        String color,
        String imageUrl,
        String description
) {
    public static ProductCategory of(String name, String color, String imageUrl, String description) {
        return new ProductCategory(0L, name, color, imageUrl, description);
    }

    public static ProductCategory from(ProductCategory category) {
        return new ProductCategory(
                category.id(),
                category.name(),
                category.color(),
                category.imageUrl(),
                category.description()
        );
    }
}
