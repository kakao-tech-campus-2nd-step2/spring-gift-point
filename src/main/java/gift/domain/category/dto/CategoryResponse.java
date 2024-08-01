package gift.domain.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 응답 Dto")
public record CategoryResponse(
    @Schema(description = "카테고리 Id")
    Long id,
    @Schema(description = "카테고리 이름")
    String name,

    @Schema(description = "카테고리 색깔")
    String color,

    @Schema(description = "카테고리 이미지 Url")
    String imageUrl,

    @Schema(description = "카테고리 설명")
    String description) {

}
