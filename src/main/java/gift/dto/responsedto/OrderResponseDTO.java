package gift.dto.responsedto;

import gift.domain.Order;
import java.time.LocalDateTime;

public record OrderResponseDTO(
    Long id,
    Long optionId,
    int quantity,
    LocalDateTime orderDateTime,
    String message
) {
    public static OrderResponseDTO from(Order order) {
        return new OrderResponseDTO(order.getId(), order.getOption().getId(), order.getQuantity(),
            order.getOrderDateTime(), order.getMessage());
    }
}
