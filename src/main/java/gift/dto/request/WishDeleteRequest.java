package gift.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WishDeleteRequest(
        @NotNull(message = "WISH LIST에 삭제하고 싶은 상품을 선택해 주세요")
        @Positive(message = "WISH LIST에 삭제하고 싶은 상품을 선택해 주세요")
        Long wish_id
) {
}
