package gift.product.restapi.dto.response;

import gift.core.domain.product.Product;

public record ProductResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        String category
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.id(),
                product.name(),
                product.price(),
                product.imageUrl(),
                product.categoryName()
        );
    }
}
