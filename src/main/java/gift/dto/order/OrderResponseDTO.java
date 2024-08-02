package gift.dto.order;

import java.time.LocalDateTime;

public record OrderResponseDTO(
        int id,
        int productId,
        String name,
        String imageUrl,
        int optionId,
        int count,
        int price,
        LocalDateTime orderDateTime,
        String message,
        boolean success) {

}
