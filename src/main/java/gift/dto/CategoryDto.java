package gift.dto;

import gift.vo.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "카테고리 DTO")
public record CategoryDto (

        @Schema(description = "카테고리 ID")
        Long id,
        
        @NotEmpty(message = "카테고리명을 입력해 주세요")
        @Schema(description = "카테고리명")
        String name,

        @NotEmpty(message = "색상을 입력해주세요")
        @Schema(description = "카테고리 색상")
        String color,

        @NotEmpty(message = "이미지 url을 입력해 주세요")
        @Schema(description = "카테고리 이미지 URL")
        String imageUrl,

        @Schema(description = "카테고리 설명")
        String description
) {
    public Category toCategory() {
        return new Category(id, name, color, imageUrl, description);
    }

    public static CategoryDto fromCategory(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getImageUrl(),
                category.getDescription());
    }
}
