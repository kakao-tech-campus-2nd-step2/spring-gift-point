package gift.dto.betweenClient.category;

import gift.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record CategoryDTO(
        Long id,

        @NotBlank(message = "카테고리 이름을 입력해주세요.")
        @Length(min = 1, max = 15, message = "카테고리명 길이는 1~15자만 가능합니다.")
        String name,

        @Pattern(regexp = "^#[0-9a-fA-F]{6}$")
        String color,

        @URL
        String imageUrl,

        String description) {
    public Category convertToCategory() {
        return new Category(name, color, imageUrl, description);
    }

    public static CategoryDTO convertToCategoryDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
    }
}
