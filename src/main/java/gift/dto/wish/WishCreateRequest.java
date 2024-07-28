package gift.dto.wish;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "위시리스트 항목 생성 요청 데이터")
public record WishCreateRequest(
    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "상품 ID", example = "1")
    Long productId
) {

}
