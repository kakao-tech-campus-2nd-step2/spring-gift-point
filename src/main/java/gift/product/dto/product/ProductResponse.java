package gift.product.dto.product;

import gift.product.dto.category.CategoryIdAndName;
import gift.product.dto.option.OptionResponse;
import java.util.List;

public record ProductResponse(
    Long id,
    String name,
    int price,
    String imageUrl,
    CategoryIdAndName category,
    List<OptionResponse> options
) {

}
