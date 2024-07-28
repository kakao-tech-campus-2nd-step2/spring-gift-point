package gift.dto.product;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "상품 생성 요청 데이터")
public record ProductCreateRequest(
    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "상품 이름", example = "Product1")
    String name,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "상품 가격", example = "1000")
    Integer price,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "상품 이미지 URL", example = "https://via.placeholder.com/150?text=product1")
    String imageUrl,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "카테고리 ID", example = "1")
    Long categoryId
) {

}
