package gift.DTO.category;

import gift.domain.Category;

public class CategoryResponse {

    private Long id;

    private String name;

    private String color;

    private String imageUrl;

    private String description;

    protected CategoryResponse() {
    }

    public CategoryResponse(Long id, String name, String color,
                            String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public static CategoryResponse fromEntity(Category categoryEntity) {
        return new CategoryResponse(
            categoryEntity.getId(),
            categoryEntity.getName(),
            categoryEntity.getColor(),
            categoryEntity.getImageUrl(),
            categoryEntity.getDescription()
        );
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
