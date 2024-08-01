package gift.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.dto.category.CategoryResponse;
import gift.dto.option.OptionResponse;

import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        @JsonProperty("category")
        CategoryResponse categoryResponse,
        List<OptionResponse> options
) {
    public static ProductResponse of(Long id, String name, Integer price, String imageUrl, CategoryResponse categoryResponse, List<OptionResponse> options) {
        return new ProductResponse(id, name, price, imageUrl, categoryResponse, options);
    }
}
