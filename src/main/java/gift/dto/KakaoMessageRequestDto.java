package gift.dto;

import gift.vo.Order;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "카카오 메시지 전송 요청 DTO")
public record KakaoMessageRequestDto (

        @Schema(description = "주문 ID")
        Long id,

        @Schema(description = "옵션 ID")
        Long optionId,

        @Schema(description = "주문 수량")
        int quantity,

        @Schema(description = "주문 날짜 및 시간")
        LocalDateTime orderDateTime,

        @Schema(description = "주문 메시지")
        String message,

        @Schema(description = "상품명")
        String productName,

        @Schema(description = "옵션명")
        String optionName
) {
    public static KakaoMessageRequestDto toKakaoMessageRequestDto(Order order, String productName, String optionName) {
        return new KakaoMessageRequestDto(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage(),
                productName,
                optionName);
    }
}
