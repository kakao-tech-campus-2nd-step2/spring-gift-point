package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "위시리스트 추가 요청 Dto")
public record WishRequestDto (

        @Schema(description = "위시리스트에 추가할 상품 ID")
        @NotNull
        Long productId

) {
}
