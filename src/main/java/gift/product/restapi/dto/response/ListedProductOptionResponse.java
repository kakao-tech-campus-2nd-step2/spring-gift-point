package gift.product.restapi.dto.response;

import gift.core.domain.product.ProductOption;

import java.util.List;

public record ListedProductOptionResponse(
        List<ProductOptionResponse> contents
) {
    public static ListedProductOptionResponse from(List<ProductOption> pagedDto) {
        return new ListedProductOptionResponse(
                pagedDto.stream()
                        .map(ProductOptionResponse::from)
                        .toList()
        );
    }
}
