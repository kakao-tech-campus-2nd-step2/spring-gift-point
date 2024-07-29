package gift.controller.category.dto;

import gift.model.Category;
import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {

    public record Create(
        @NotBlank
        String name,
        @NotBlank
        String color,
        @NotBlank
        String imageUrl,
        @NotBlank
        String description
    ) {

        public Category toEntity() {
            return new Category(null, name, color, imageUrl, description);
        }
    }

    public record Update(
        @NotBlank
        String name,
        @NotBlank
        String color,
        @NotBlank
        String imageUrl,
        @NotBlank
        String description
    ) {
    }
}
