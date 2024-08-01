package gift.product.application.dto.response;

import gift.product.service.dto.ProductInfo;

public record ProductResponse(
        Long id,
        String name,
        Integer price,
        String imgUrl,
        String categoryName
) {
    public static ProductResponse from(ProductInfo productInfo) {
        return new ProductResponse(productInfo.id(), productInfo.name(), productInfo.price(), productInfo.imgUrl(),
                productInfo.category().name());
    }
}
