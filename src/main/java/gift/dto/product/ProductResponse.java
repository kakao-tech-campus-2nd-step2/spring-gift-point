package gift.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.dto.category.CategoryInformation;
import gift.dto.option.OptionResponse;

import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        @JsonProperty("category")
        CategoryInformation categoryInformation,
        List<OptionResponse> options
) {
    public static ProductResponse of(Long id, String name, Integer price, String imageUrl, CategoryInformation categoryInformation, List<OptionResponse> options) {
        return new ProductResponse(id, name, price, imageUrl, categoryInformation, options);
    }
}
