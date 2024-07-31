package gift.domain.product.dto;

import gift.domain.option.dto.OptionResponse;
import java.util.List;

public record ProductDetailResponse(
    Long id,
    String name,
    int price,
    String imageUrl,
    List<OptionResponse> options
) {
}
