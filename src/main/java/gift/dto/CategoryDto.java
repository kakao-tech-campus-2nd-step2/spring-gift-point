package gift.dto;

import gift.domain.Category;
import jakarta.validation.constraints.NotNull;

public class CategoryDto {
    private Long id;
    @NotNull(message = "카테고리 name 입력은 필수 입니다.")
    private String name;
    @NotNull(message = "카테고리 color 입력은 필수 입니다.")
    private String color;
    @NotNull(message = "카테고리 imageUrl 입력은 필수 입니다.")
    private String imageUrl;
    private String description;

    public CategoryDto() {
    }

    public CategoryDto(Long id, String name, String color, String imageUrl,String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
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
