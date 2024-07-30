package gift.Model.DTO;

import java.time.LocalDateTime;

public record OrderDTO(Long id, Long optionId, Long quantity, LocalDateTime orderDateTime, String message) {
}
