package gift.dto;

import gift.vo.Order;

import java.time.LocalDateTime;

public record OrderResponseDto (
        Long id,
        Long optionId,
        int quantity,
        LocalDateTime orderDateTime,
        String message
) {
    public static OrderResponseDto toOrderResponseDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage());
    }
}
