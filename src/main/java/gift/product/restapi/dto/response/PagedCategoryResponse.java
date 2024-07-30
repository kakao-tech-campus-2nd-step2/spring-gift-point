package gift.product.restapi.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.core.PagedDto;
import gift.core.domain.product.ProductCategory;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PagedCategoryResponse(
        List<ProductCategoryResponse> contents
) {
    public static PagedCategoryResponse from(PagedDto<ProductCategory> pagedDto) {
        return new PagedCategoryResponse(
                pagedDto.contents().stream()
                        .map(ProductCategoryResponse::of)
                        .toList()
        );
    }
}
