package gift.order;

import java.time.LocalDateTime;

public record OrderResponseDto(
    Long id,
    Long optionId,
    int quantity,
    LocalDateTime localDateTime,
    String message) {
}
