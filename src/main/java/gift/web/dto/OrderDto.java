package gift.web.dto;

import java.time.LocalDateTime;

public record OrderDto(
    Long id,
    Long optionId,
    Long quantity,
    LocalDateTime orderDateTime,
    String message
)
{ }
