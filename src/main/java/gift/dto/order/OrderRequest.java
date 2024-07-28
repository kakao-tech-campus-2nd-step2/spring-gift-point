package gift.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 요청 데이터")
public record OrderRequest(
    @Schema(description = "옵션 ID", example = "1")
    Long optionId,

    @Schema(description = "주문 수량", example = "2")
    int quantity,

    @Schema(description = "주문 메시지", example = "테스트 메시지")
    String message
) {

}
