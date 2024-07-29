package gift.wishes.restapi.dto.response;

import gift.core.PagedDto;
import gift.core.domain.product.Product;

import java.util.List;

public record PagedWishResponse(
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages,
        List<WishResponse> contents
) {
    public static PagedWishResponse from(PagedDto<Product> pagedDto) {
        return new PagedWishResponse(
                pagedDto.page(),
                pagedDto.size(),
                pagedDto.totalElements(),
                pagedDto.totalPages(),
                pagedDto.contents().stream()
                        .map(WishResponse::from)
                        .toList()
        );
    }
}
