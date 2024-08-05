package gift.order.dto;

import java.time.LocalDateTime;

public record OrderPageResponse(
        Long id,
        Long optionId,
        String optionName,
        String productName,
        Integer productPrice,
        String imageUrl,
        Long quantity,
        LocalDateTime orderDateTime,
        String message
) {
}
