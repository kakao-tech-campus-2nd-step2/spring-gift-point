package gift.dto.betweenClient.order;

import gift.entity.Option;
import gift.entity.OrderHistory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record OrderRequestDTO(

    @NotBlank(message = "옵션 아이디를 입력해주세요")
    Long optionId,

    @NotBlank(message = "수량을 입력해주세요")
    @Min(value = 1, message = "주문 옵션 수량은 최소 1이여야 합니다.")
    Integer quantity,

    @NotBlank(message = "메세지를 입력해주세요")
    String message
) {
    public OrderHistory convertToOrder(Option option, LocalDateTime localDateTime) {
        return new OrderHistory(option, quantity, localDateTime, message);
    }
}
