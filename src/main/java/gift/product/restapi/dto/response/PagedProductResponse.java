package gift.product.restapi.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.core.PagedDto;
import gift.core.domain.product.Product;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PagedProductResponse(
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages,
        List<ProductResponse> contents
) {
    public static PagedProductResponse from(PagedDto<Product> pagedDto) {
        return new PagedProductResponse(
                pagedDto.page(),
                pagedDto.size(),
                pagedDto.totalElements(),
                pagedDto.totalPages(),
                pagedDto.contents().stream()
                        .map(ProductResponse::from)
                        .toList()
        );
    }
}
