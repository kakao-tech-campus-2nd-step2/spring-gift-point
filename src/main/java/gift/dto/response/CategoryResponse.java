package gift.dto.response;


import gift.entity.Category;
import gift.entity.Option;

public class CategoryResponse {
    private Long categoryId;
    private String name;
    private String imageUrl;
    private String description;

    // Getters and Setters

    public CategoryResponse(Long categoryId, String name, String imageUrl, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public CategoryResponse(Category category) {
        this.categoryId = category.getId();
        this.name = category.getName();
        this.imageUrl = category.getImageUrl();
        this.description = category.getDescription();
    }

    public CategoryResponse(Option option) {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
