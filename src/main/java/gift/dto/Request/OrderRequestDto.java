package gift.dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 요청 DTO")
public record OrderRequestDto(
    @Schema(description = "옵션 ID", example = "1") Long optionId,
    @Schema(description = "주문 수량", example = "2") int quantity,
    @Schema(description = "주문 메시지", example = "빠른 배송 부탁드립니다.") String message
) {}
