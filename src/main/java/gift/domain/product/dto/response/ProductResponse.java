package gift.domain.product.dto.response;

import gift.domain.option.dto.response.OptionResponse;
import java.util.List;

public record ProductResponse(
    Long id,
    String name,
    int price,
    String description,
    String imageUrl,
    List<OptionResponse> options
) {

}
