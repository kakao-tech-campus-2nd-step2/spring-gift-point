package gift.dto;

import jakarta.validation.constraints.NotNull;

public record WishDto(
    @NotNull(message = "상품 id 입력은 필수 입니다.")
    Long productId
) {

}
