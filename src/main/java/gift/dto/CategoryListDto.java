package gift.dto;

import gift.domain.Category;

public class CategoryListDto {
    private final Long id;
    private final String name;
    private final String color;
    private final String image_url;
    private final String description;

    public CategoryListDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.color = category.getColor();
        this.image_url = category.getImage_url();
        this.description = category.getDescription();
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
    public String getImage_url() {
        return image_url;
    }
    public String getDescription() {
        return description;
    }
}
