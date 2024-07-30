package gift.domain.order.dto;

import gift.domain.order.entity.Order;
import gift.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "주문 요청 정보")
public record OrderRequest(

    @NotEmpty(message = "주문하실 상품을 한 개 이상 선택해주세요.")
    @Schema(description = "주문 상품 목록", implementation = OrderItemRequest.class)
    List<@Valid OrderItemRequest> orderItems,

    @Size(max = 255, message = "메시지는 255자를 초과할 수 없습니다.")
    @Schema(description = "수신자 메시지")
    String recipientMessage,

    @Min(value = 0, message = "총 구매 가격은 음수일 수 없습니다.")
    @Schema(description = "주문 총액")
    int totalPrice
) {
    public Order toOrder(User user) {
        return new Order(null, user, recipientMessage, totalPrice);
    }
}
