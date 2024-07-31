package gift.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 응답 Dto")
public record OrderResponse(

    @Schema(description = "주문 Id")
    Long id,
    @Schema(description = "옵션 Id")
    Long optionId,
    @Schema(description = "옵션 수량")
    int quantity,
    @Schema(description = "주문 생성 시간")
    String orderDateTime,
    @Schema(description = "주문 메세지")
    String message
) {

}
