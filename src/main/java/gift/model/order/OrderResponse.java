package gift.model.order;

import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    Long optionId,
    int quantity,
    String message,
    LocalDateTime orderDateTime
) {}
