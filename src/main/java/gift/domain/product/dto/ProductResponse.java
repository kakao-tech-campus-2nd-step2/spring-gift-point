package gift.domain.product.dto;

import gift.domain.product.entity.Product;

public record ProductResponse(
    Long id,
    String name,
    int price,
    String imageUrl,
    Long categoryId,
    String categoryName
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId(),
            product.getCategory().getName()
        );
    }
}
