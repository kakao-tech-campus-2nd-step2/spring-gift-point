package gift.domain.order.dto.response;

import java.time.LocalDateTime;

public record OrderResponse(
    Long orderId,
    Long productId,
    String productName,
    String optionName,
    Long quantity,
    LocalDateTime date,
    int price,
    String imageUrl
) {

}