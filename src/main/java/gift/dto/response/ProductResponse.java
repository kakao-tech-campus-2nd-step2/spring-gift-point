package gift.dto.response;

import gift.entity.Product;

public record ProductResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        Long categoryId,
        String categoryName
) {
    public static ProductResponse fromProduct(Product product) {
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
