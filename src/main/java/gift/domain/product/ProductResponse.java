package gift.domain.product;

public record ProductResponse(
    Long id,
    String name,
    Long price,
    String imageUrl,
    Long categoryId
) {

    public ProductResponse(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(),
            product.getCategory().getId());
    }
}
