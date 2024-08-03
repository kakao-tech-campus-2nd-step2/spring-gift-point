package gift.product.application.dto.response;

import gift.product.service.dto.ProductPageInfo;
import java.util.List;
import org.springframework.data.domain.Pageable;

public record ProductPageResponse(
        List<ProductResponse> contents,
        Pageable pageable,
        Integer totalPages,
        Long totalElements,
        Boolean last,
        Integer number,
        Integer size,
        Integer numberOfElements,
        Boolean first,
        Boolean empty
) {
    public static ProductPageResponse from(ProductPageInfo productPageInfo) {
        var products = productPageInfo.products().stream()
                .map(ProductResponse::from)
                .toList();
        return new ProductPageResponse(
                products,
                productPageInfo.pageable(),
                productPageInfo.totalPages(),
                productPageInfo.totalElements(),
                productPageInfo.last(),
                productPageInfo.number(),
                productPageInfo.size(),
                productPageInfo.numberOfElements(),
                productPageInfo.first(),
                productPageInfo.empty()
        );
    }
}
