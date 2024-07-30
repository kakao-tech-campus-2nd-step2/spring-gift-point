package gift.response;

import java.util.List;

public record ProductListResponse(
    List<ProductResponse> contents,
    int page,
    int size,
    Long totalElements,
    int totalPages
) {

}
