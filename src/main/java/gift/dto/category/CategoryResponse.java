package gift.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 응답 데이터")
public record CategoryResponse(
    @Schema(description = "카테고리 ID", example = "1")
    Long id,

    @Schema(description = "카테고리 이름", example = "교환권")
    String name,

    @Schema(description = "카테고리 색상", example = "#F15F5F")
    String color,

    @Schema(description = "카테고리 이미지 URL", example = "https://via.placeholder.com/150?text=category")
    String imageUrl,

    @Schema(description = "카테고리 설명", example = "카테고리 설명 테스트입니다.")
    String description
) {

}
