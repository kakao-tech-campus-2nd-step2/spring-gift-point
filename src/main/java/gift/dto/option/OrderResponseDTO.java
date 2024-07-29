package gift.dto.option;

import java.time.LocalDateTime;

public record OrderResponseDTO(int optionId, int quantity, LocalDateTime orderDateTime, String message) {
}
