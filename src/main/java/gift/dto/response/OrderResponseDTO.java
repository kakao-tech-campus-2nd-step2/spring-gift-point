package gift.dto.response;

import java.time.LocalDateTime;

public record OrderResponseDTO(Long id,
                               Long productId,
                               String name,
                               String imageUrl,
                               Long optionId,
                               Integer count,
                               Long price,
                               LocalDateTime orderDateTime,
                               String message,
                               boolean success) {
}

