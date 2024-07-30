package gift.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrderRequest(
        @NotNull(message = "옵션ID를 입력해주세요.")
        Long optionId,
        @NotNull(message = "주문할 수량을 입력해주세요.")
        Integer quantity,
        @Size(max = 200, message = "최대 200자까지 메세지에 입력가능합니다.")
        String message
) {
}
