package gift.dto.request;

import java.time.LocalDateTime;

public record OrderRequestDTO(Long optionId, Integer quantity, String message) {
}
