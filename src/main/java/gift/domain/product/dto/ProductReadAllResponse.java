package gift.domain.product.dto;

import gift.domain.product.entity.Product;

public record ProductReadAllResponse(
    Long id,
    CategoryResponse category,
    String name,
    int price,
    String imageUrl
) {
    public static ProductReadAllResponse from(Product product) {
        return new ProductReadAllResponse(
            product.getId(),
            CategoryResponse.from(product.getCategory()),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }
}
