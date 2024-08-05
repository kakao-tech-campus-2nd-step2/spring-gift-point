package gift.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ProductListResponse(
    List<ProductResponse> contents,
    int page,
    int size,
    @JsonProperty(value = "total_elements")
    Long totalElements,
    @JsonProperty(value = "total_pages")
    int totalPages
) {

}
