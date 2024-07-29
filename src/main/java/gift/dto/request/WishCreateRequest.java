package gift.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WishCreateRequest(
        @NotNull(message = "WISH LIST에 추가하고 싶은 상품을 선택해 주세요")
        @Positive(message = "WISH LIST에 추가하고 싶은 상품을 선택해 주세요")
        Long product_id,

        @NotNull(message = "개수을 입력하세요 (1 이상)")
        @Positive(message = "개수은 1개 이상이어야 합니다")
        int count) { }
