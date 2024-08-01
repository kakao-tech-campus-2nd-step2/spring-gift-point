package gift.DTO.order;

import gift.domain.Order;
import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    Long memberId,
    Long optionId,
    Long quantity,
    String message,
    LocalDateTime orderDateTime
    ) {
    public static OrderResponse fromEntity(Order orderEntity) {
        return new OrderResponse(
            orderEntity.getId(),
            orderEntity.getMember().getId(),
            orderEntity.getOption().getId(),
            orderEntity.getQuantity(),
            orderEntity.getMessage(),
            orderEntity.getOrderDateTime()
        );
    }
}
