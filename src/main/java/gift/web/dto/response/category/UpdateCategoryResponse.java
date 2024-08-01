package gift.web.dto.response.category;

import gift.domain.Category;

public class UpdateCategoryResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final String color;

    public UpdateCategoryResponse(Long id, String name, String description, String imageUrl,
        String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.color = color;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getColor() {
        return color;
    }

    public static UpdateCategoryResponse fromEntity(Category category) {
        return new UpdateCategoryResponse(
            category.getId(), category.getName(), category.getDescription(),
            category.getImageUrl().toString(), category.getColor().toString());
    }
}
