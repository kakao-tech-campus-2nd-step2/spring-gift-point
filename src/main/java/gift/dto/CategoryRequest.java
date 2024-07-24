package gift.dto;

public class CategoryRequest {
    private String name;
    private String color;
    private String description;
    private String imageUrl;

    public CategoryRequest() {
    }

    public CategoryRequest(String name, String color, String description, String imageUrl) {
        this.name = name;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
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
}
