package gift.domain.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CategoryRequest(
    @NotBlank(message = "카테고리명이 입력되지 않았습니다")
    String name,

    @NotBlank(message = "색상코드가 입력되지 않았습니다")
    @Pattern(regexp = "#[\\da-fA-F]{6}", message = "카테고리 색상 코드가 올바르지 않습니다")
    String color,

    @NotBlank(message = "카테고리 이미지가 입력되지 않았습니다")
    String imageUrl,

    String description
) {

    public Category toCategory() {
        return new Category(name, color, imageUrl, description);
    }
}