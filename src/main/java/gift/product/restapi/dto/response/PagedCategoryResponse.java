package gift.product.restapi.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.core.PagedDto;
import gift.core.domain.product.ProductCategory;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PagedCategoryResponse(
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages,
        List<ProductCategoryResponse> contents
) {
    public static PagedCategoryResponse from(PagedDto<ProductCategory> pagedDto) {
        return new PagedCategoryResponse(
                pagedDto.page(),
                pagedDto.size(),
                pagedDto.totalElements(),
                pagedDto.totalPages(),
                pagedDto.contents().stream()
                        .map(ProductCategoryResponse::of)
                        .toList()
        );
    }
}
