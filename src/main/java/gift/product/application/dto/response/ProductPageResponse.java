package gift.product.application.dto.response;

import gift.product.service.dto.ProductPageInfo;
import java.util.List;

public record ProductPageResponse(
        List<ProductResponse> products,
        long totalElements,
        int totalPages,
        int currentPage
) {
    public static ProductPageResponse from(ProductPageInfo productPageInfo) {
        var products = productPageInfo.products().stream()
                .map(ProductResponse::from)
                .toList();
        return new ProductPageResponse(
                products,
                productPageInfo.totalElements(),
                productPageInfo.totalPages(),
                productPageInfo.currentPage()
        );
    }
}
