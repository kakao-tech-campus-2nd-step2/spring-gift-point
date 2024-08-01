package gift.dto.betweenClient.order;

import gift.entity.OrderHistory;
import java.time.LocalDateTime;

public record OrderResponseDTO(
        Long id,
        Long optionId,
        Integer quantity,
        LocalDateTime orderDateTime,
        String message
) {
    public static OrderResponseDTO convertToDTO(OrderHistory orderHistory) {
        return new OrderResponseDTO(orderHistory.getId(), orderHistory.getOption().getId(),
                orderHistory.getQuantity(), orderHistory.getOrderDateTime(), orderHistory.getMessage());
    }
}
