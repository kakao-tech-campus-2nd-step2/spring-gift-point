package gift.dto.response;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long optionId,
        Integer quantity,
        LocalDateTime orderDateTime,
        String message
) {
}
