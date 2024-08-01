package gift.dto.product;

import java.util.List;

public record ProductPageResponse(Integer page, Integer size, Long totalElements, Integer totalPages, List<ProductResponse> content) {
}
