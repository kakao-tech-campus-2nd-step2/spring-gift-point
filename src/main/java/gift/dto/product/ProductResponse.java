package gift.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.dto.category.CategoryResponse;

public record ProductResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        @JsonProperty("category")
        CategoryResponse categoryResponse
) {
    public static ProductResponse of(Long id, String name, Integer price, String imageUrl, CategoryResponse categoryResponse) {
        return new ProductResponse(id, name, price, imageUrl, categoryResponse);
    }
}
