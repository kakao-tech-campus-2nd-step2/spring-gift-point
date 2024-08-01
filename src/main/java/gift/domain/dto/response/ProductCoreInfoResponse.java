package gift.domain.dto.response;

import gift.domain.entity.Product;

public record ProductCoreInfoResponse(
    Long id,
    String name,
    Integer price,
    String imageUrl
) {

    public static ProductCoreInfoResponse of(Product product) {
        return new ProductCoreInfoResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl());
    }

    public static ProductCoreInfoResponse of(ProductResponse productResponse) {
        return new ProductCoreInfoResponse(
            productResponse.id(),
            productResponse.name(),
            productResponse.price(),
            productResponse.imageUrl());
    }
}
