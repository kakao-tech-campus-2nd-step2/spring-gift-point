package gift.dto;

import gift.vo.Order;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "상품 주문 응답 DTO")
public record OrderResponseDto (

        @Schema(description = "주문 ID")
        Long id,

        @Schema(description = "옵션 ID")
        Long optionId,

        @Schema(description = "주문 수량")
        int quantity,

        @Schema(description = "주문 일시")
        LocalDateTime orderDateTime,

        @Schema(description = "주문 메시지")
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
