package gift.product.application.dto.response;

import gift.product.service.dto.ProductInfo;

public record ProductResponse(
        Long id,
        String name,
        Integer price,
        String imgUrl,
        CategoryResponse category
) {
    public static ProductResponse from(ProductInfo productInfo) {
        var category = CategoryResponse.from(productInfo.category());
        return new ProductResponse(productInfo.id(), productInfo.name(), productInfo.price(), productInfo.imgUrl(),
                category);
    }
}
