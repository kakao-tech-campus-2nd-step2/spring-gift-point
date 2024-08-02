package gift.dto;

import gift.domain.Category;

public class CategoryResponseDto {
    private Long id;
    private String color;
    private String description;
    private String imageUrl;
    private String name;

    public CategoryResponseDto(Long id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public Category toEntity(){
        return new Category(id,name,color,description,imageUrl);
    }
}
