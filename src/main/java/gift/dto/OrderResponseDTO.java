package gift.dto;

import java.time.LocalDateTime;

public record OrderResponseDTO(
    Long id,
    Long optionId,
    Long quantity,
    LocalDateTime orderDateTime,
    String message
) {

}
