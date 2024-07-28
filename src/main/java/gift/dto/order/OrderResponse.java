package gift.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "주문 응답 데이터")
public record OrderResponse(
    @Schema(description = "주문 ID", example = "1")
    Long id,

    @Schema(description = "옵션 ID", example = "1")
    Long optionId,

    @Schema(description = "주문 수량", example = "2")
    int quantity,

    @Schema(description = "주문 시간", example = "2024-07-28T19:10:00")
    LocalDateTime orderDateTime,

    @Schema(description = "주문 메시지", example = "테스트 메시지")
    String message
) {

}
