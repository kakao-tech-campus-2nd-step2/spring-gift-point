package gift.DTO.Category;

import gift.domain.Category;

public class CategoryResponse {
    Long id;
    String name;
    String description;
    String color;
    String imageUrl;

    public CategoryResponse(){

    }

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.color = category.getColor();
        this.imageUrl = category.getImageUrl();
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
}
