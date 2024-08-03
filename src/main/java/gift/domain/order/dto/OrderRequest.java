package gift.domain.order.dto;

import gift.domain.member.entity.Member;
import gift.domain.order.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "주문 요청 정보")
public record OrderRequest(

    @NotNull(message = "상품을 선택해주세요.")
    @Schema(description = "상품 ID")
    Long optionId,

    @NotNull(message = "옵션을 선택해주세요.")
    @Schema(description = "옵션 ID")
    int quantity,

    @Size(max = 255, message = "메시지는 255자를 초과할 수 없습니다.")
    @Schema(description = "수신자 메시지")
    String message
) {
    public Order toOrder(Member member) {
        return new Order(null, member, message);
    }
}
