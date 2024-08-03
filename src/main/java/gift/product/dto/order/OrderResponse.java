package gift.product.dto.order;

import gift.product.dto.option.OptionInfoForOrderResponse;
import gift.product.dto.product.ProductInfoForOrderResponse;
import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    ProductInfoForOrderResponse product,
    OptionInfoForOrderResponse option,
    LocalDateTime orderDateTime
) {

}
