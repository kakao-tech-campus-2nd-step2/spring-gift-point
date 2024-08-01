package gift.product.dto.response;

import gift.product.entity.Product;

public record ProductResponse(
    Long id,
    String name,
    Integer price,
    String imageUrl,
    Long categoryId
) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategoryId()
        );
    }

}
