package gift.order.dto;

import java.time.LocalDateTime;

public record OrderPageResponse(
        Long id,
        Long optionId,
        Long quantity,
        LocalDateTime orderDateTime,
        String message
) {
}
