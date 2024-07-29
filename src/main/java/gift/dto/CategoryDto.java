package gift.dto;

import gift.vo.Category;
import jakarta.validation.constraints.NotEmpty;

public record CategoryDto (
        Long id,
        
        @NotEmpty(message = "카테고리명을 입력해 주세요")
        String name,

        @NotEmpty(message = "색상을 입력해주세요")
        String color,

        @NotEmpty(message = "이미지 url을 입력해 주세요")
        String imageUrl,

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
