package gift.dto.Response;

import gift.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "주문 응답 DTO")
public record OrderResponseDto(
    @Schema(description = "주문 ID", example = "1") Long id,
    @Schema(description = "옵션 ID", example = "1") Long optionId,
    @Schema(description = "주문 수량", example = "2") int quantity,
    @Schema(description = "주문 시간", example = "2024-07-26T12:34:56") LocalDateTime orderDateTime,
    @Schema(description = "주문 메시지", example = "빠른 배송 부탁드립니다.") String message
) {
    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
            order.getId(),
            order.getOption().getId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }
}
