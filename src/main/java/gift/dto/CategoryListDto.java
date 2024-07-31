package gift.dto;

import gift.domain.Category;

public class CategoryListDto {
    private Long id;
    private String name;
    private String color;
    private String image_url;
    private String description;

    public CategoryListDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.color = category.getColor();
        this.image_url = category.getImage_url();
        this.description = category.getDescription();
    }
}
