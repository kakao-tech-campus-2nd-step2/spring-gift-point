package gift.dto.response;

import java.time.LocalDateTime;

public record OrderSuccessResponseDTO(Long id,
                                      Long optionId,
                                      Integer quantity,
                                      LocalDateTime orderDateTime,
                                      String message,
                                      boolean success) {
}


