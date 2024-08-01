package gift.dto.wishproduct;

import java.util.List;

public record WishProductPageResponse(Integer page, Integer size, Long totalElements, Integer totalPages, List<WishProductResponse> content) {
}
