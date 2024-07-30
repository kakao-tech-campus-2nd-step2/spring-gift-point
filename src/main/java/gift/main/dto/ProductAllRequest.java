package gift.main.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProductAllRequest(
        String name,
        int price,
        String imageUrl,
        int categoryId,
        List<OptionRequest> options) {

    public ProductAllRequest(ProductRequest productRequest,
                             OptionListRequest optionListRequest) {
        this(
                productRequest.name(),
                productRequest.price(),
                productRequest.imageUrl(),
                productRequest.categoryId(),
                optionListRequest.options()
        );
    }


}
