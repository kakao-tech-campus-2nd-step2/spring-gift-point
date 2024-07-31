package gift.domain.model.dto;

import gift.domain.model.entity.Category;

public class CategoryResponseDto {

    private final Long id;
    private final String name;
    private final String color;
    private final String imageUrl;
    private final String description;

    public CategoryResponseDto(Long id, String name, String color, String imageUrl,
        String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public static CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName(), category.getColor(),
            category.getImageUrl(),
            category.getDescription());
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
}
