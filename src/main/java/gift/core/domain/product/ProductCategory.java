package gift.core.domain.product;

public record ProductCategory(
        Long id,
        String name
) {
    public static ProductCategory of(String name) {
        return new ProductCategory(0L, name);
    }

    public static ProductCategory from(ProductCategory category) {
        return new ProductCategory(category.id(), category.name());
    }
}
