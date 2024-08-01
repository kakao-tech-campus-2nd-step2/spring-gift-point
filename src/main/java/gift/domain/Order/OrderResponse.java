package gift.domain.Order;

import java.util.Date;

public record OrderResponse(
        Long id,
        Long optionId,
        Long quantity,
        Date orderDateTime,
        String message
        ) {
}