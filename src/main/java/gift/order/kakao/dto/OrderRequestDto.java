package gift.order.kakao.dto;

import gift.global.model.MultiValueMapConvertibleDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// 주문 넣을 dto
public record OrderRequestDto(
    @NotNull(message = "option id는 필수입니다.")
    long optionId,

    @Min(value = 1, message = "최소 한 개 이상의 제품을 구매해야 합니다.")
    @Max(value = 100_000_000, message = "1억개를 초과해서 제품을 구매할 수 없습니다.")
    @NotNull(message = "구매 수량이 누락되었습니다.")
    int quantity,

    @Size(max = 200, message = "메시지는 200자를 초과할 수 없습니다.")
    String message
) implements MultiValueMapConvertibleDto {

}
