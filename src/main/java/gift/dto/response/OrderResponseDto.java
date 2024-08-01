package gift.dto.response;

import gift.domain.Order;

import java.time.LocalDateTime;

public record OrderResponseDto(
        Long id,
        Long optionId,
        int quantity,
        LocalDateTime orderDateTime,
        String message
) {
    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(order.getId(),
                order.getOption().getId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage());
    }
}
