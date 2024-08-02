package gift.domain.product.dto;

import gift.domain.product.entity.Product;

public record ProductReadAllResponse(
    Long id,
    String name,
    int price,
    String imageUrl,
    Long categoryId,
    String categoryName
) {
    public static ProductReadAllResponse from(Product product) {
        return new ProductReadAllResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId(),
            product.getCategory().getName()
        );
    }
}
