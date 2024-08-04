package gift.dto.betweenClient.category;

import gift.entity.Category;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CategoryDTO(
        Long id,

        @NotBlank(message = "카테고리 이름을 입력해주세요.")
        String name,

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
