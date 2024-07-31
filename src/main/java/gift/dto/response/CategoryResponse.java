package gift.dto.response;

import gift.domain.Category;

public class CategoryResponse {

    private Long id;
    private String name;
    private String color;
    private String imageUrl;
    private String description;

    public CategoryResponse(Long id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public static CategoryResponse fromCategory(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getImageUrl(),
                category.getDescription()
        );
    }

    public String getName() {
        return name;
    }

    public Long getId(){
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public  String getDescription() {
        return description;
    }
}
