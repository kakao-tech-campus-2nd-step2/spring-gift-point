package gift.dto.category;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "카테고리 생성 요청 데이터")
public record CategoryCreateRequest(
    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "카테고리 이름", example = "교환권")
    String name,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "카테고리 색상", example = "#F15F5F")
    String color,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "카테고리 이미지 URL", example = "https://via.placeholder.com/150?text=category")
    String imageUrl,

    @Schema(description = "카테고리 설명", example = "카테고리 설명 테스트입니다.")
    String description
) {

}
