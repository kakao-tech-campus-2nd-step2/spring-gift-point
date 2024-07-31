package gift.dto;

import gift.domain.Category;
import gift.dto.response.CategoryResponse;

public class CategoryDto {

    private Long id;
    private String name;
    private String color;
    private String imageUrl;
    private String description;

    public CategoryDto(String name) {
        this.name = name;
    }

    public CategoryDto(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public CategoryDto(Long id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
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

    public CategoryResponse toResponseDto() {
        return new CategoryResponse(this.id, this.name, this.color, this.imageUrl, this.description);
    }

    public Category toEntity() {
        return new Category(this.name, this.color, this.imageUrl, this.description);
    }

}
