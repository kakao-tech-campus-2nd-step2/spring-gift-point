package gift.domain;

import java.util.Date;

public record OrderResponse(
        Long id,
        Long optionId,
        Long quantity,
        Date orderDateTime,
        String message
        ) {
}
