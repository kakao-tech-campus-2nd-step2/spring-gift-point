package gift.dto;

import gift.model.Category;

public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String color;
    private String imageUrl;

    public CategoryResponseDTO(Long id, String name, String description, String color, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
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

    public static CategoryResponseDTO fromEntity(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName(), category.getDescription(), category.getColor(), category.getImageUrl());
    }
}
