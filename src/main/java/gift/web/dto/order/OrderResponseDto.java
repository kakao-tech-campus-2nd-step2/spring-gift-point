package gift.web.dto.order;

import java.time.LocalDateTime;

public record OrderResponseDto(
    Long id,
    Long optionId,
    Long quantity,
    LocalDateTime orderDateTime,
    String message
)
{ }
