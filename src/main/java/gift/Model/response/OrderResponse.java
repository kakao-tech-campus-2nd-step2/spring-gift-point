package gift.Model.response;

import java.time.LocalDateTime;

public record OrderResponse(Long id, Long optionId, Long quantity, LocalDateTime orderDateTime, String message) {
}
