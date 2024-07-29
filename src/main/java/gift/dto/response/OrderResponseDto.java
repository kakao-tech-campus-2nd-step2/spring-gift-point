package gift.dto.response;

import gift.domain.Order;

public record OrderResponseDto(
        Long id,
        Long optionId,
        int quantity,
        String orderDateTime,
        String message
) {
    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(order.getId(),
                order.getOption().getId(),
                order.getQuantity(),
                order.getCreatedAt().toString(),
                order.getMessage());
    }
}
