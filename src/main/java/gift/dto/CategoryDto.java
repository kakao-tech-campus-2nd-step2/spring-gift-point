package gift.dto;

import gift.domain.Category;
import jakarta.validation.constraints.NotNull;

public record CategoryDto(Long id,
                          @NotNull(message = "카테고리 name 입력은 필수 입니다.")
                          String name,
                          @NotNull(message = "카테고리 color 입력은 필수 입니다.")
                          String color,
                          @NotNull(message = "카테고리 imageUrl 입력은 필수 입니다.")
                          String imageUrl,
                          String description) {

    public CategoryDto(Long id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String color() {
        return color;
    }

    @Override
    public String imageUrl() {
        return imageUrl;
    }

    public static CategoryDto convertToDto(Category category) {
        return new CategoryDto(
            category.getId(),
            category.getName(),
            category.getColor(),
            category.getImageUrl(),
            category.getDescription()
        );
    }
}
