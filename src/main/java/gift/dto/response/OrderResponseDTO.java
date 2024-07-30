package gift.dto.response;

import java.time.LocalDateTime;

public record OrderResponseDTO(Long orderId,
                               Long optionId,
                               Integer quantity,
                               LocalDateTime orderDateTime,
                               String message) {
}

