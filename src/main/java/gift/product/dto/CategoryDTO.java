package gift.product.dto;

import gift.product.model.Category;
import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

    @NotBlank(message = "카테고리 이름은 필수 입력사항 입니다.")
    private String name;

    @NotBlank(message = "카테고리 색상은 필수 입력사항 입니다.")
    private String color;

    @NotBlank(message = "카테고리 이미지는 필수 입력사항 입니다.")
    private String imageUrl;

    @NotBlank(message = "카테고리 설명은 필수 입력사항 입니다.")
    private String description;

    public CategoryDTO() {}

    public CategoryDTO(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public @NotBlank(message = "카테고리 이름은 필수 입력사항 입니다.") String getName() {
        return name;
    }

    public void setName(
        @NotBlank(message = "카테고리 이름은 필수 입력사항 입니다.")String name) {
        this.name = name;
    }

    public @NotBlank(message = "카테고리 색상은 필수 입력사항 입니다.") String getColor() {
        return color;
    }

    public void setColor(
        @NotBlank(message = "카테고리 색상은 필수 입력사항 입니다.") String color) {
        this.color = color;
    }

    public @NotBlank(message = "카테고리 이미지는 필수 입력사항 입니다.") String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(
        @NotBlank(message = "카테고리 이미지는 필수 입력사항 입니다.") String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public @NotBlank(message = "카테고리 설명은 필수 입력사항 입니다.") String getDescription() {
        return description;
    }

    public void setDescription(
        @NotBlank(message = "카테고리 설명은 필수 입력사항 입니다.") String description) {
        this.description = description;
    }

    public Category convertToDomain() {
        return new Category(
            this.name,
            this.color,
            this.imageUrl,
            this.description
        );
    }

    public Category convertToDomain(Long id) {
        return new Category(
            id,
            this.name,
            this.color,
            this.imageUrl,
            this.description
        );
    }
}
