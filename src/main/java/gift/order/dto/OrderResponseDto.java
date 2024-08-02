package gift.order.dto;

import gift.order.entity.Order;
import java.time.LocalDateTime;

public record OrderResponseDto(
    Long id,
    Long optionId,
    int quantity,
    LocalDateTime orderDateTime,
    String message
) {
  public static OrderResponseDto toDto(Order order) {
    return new OrderResponseDto(
        order.getId(),
        order.getOption().getId(),
        order.getQuantity(),
        order.getOrderDateTime(),
        order.getMessage()
    );
  }
}