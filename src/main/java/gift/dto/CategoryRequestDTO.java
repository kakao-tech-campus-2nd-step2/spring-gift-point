package gift.dto;

import gift.model.Category;
import jakarta.validation.constraints.NotBlank;

public class CategoryRequestDTO {
    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank(message = "설명을 입력해주세요")
    private String description;

    @NotBlank(message = "색상을 입력해주세요")
    private String color;

    @NotBlank(message = "이미지 URL을 입력해주세요")
    private String imageUrl;

    public CategoryRequestDTO() {
    }

    public CategoryRequestDTO(String name, String description, String color, String imageUrl) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Category toEntity() {
        return new Category(this.name, this.description, this.color, this.imageUrl);
    }
}
