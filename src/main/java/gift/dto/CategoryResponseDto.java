package gift.dto;

import gift.entity.Category;

public class CategoryResponseDto {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final String color;
    private final String description;

    public CategoryResponseDto(Long id, String name, String imageUrl, String color,
        String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.color = color;
        this.description = description;
    }

    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.imageUrl = category.getImageUrl();
        this.color = category.getColor();
        this.description = category.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }
}
