package gift.product.domain;

public class CreateCategoryRequest {

    private String name;
    private String description;
    private String imageUrl;

    private String color;

    public CreateCategoryRequest(String name, String description, String imageUrl, String color) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.color = color;
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
}
