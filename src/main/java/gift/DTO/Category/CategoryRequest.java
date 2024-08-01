package gift.DTO.Category;

public class CategoryRequest {
    String name;
    String description;
    String color;
    String imageUrl;

    public CategoryRequest(){

    }

    public CategoryRequest(String name, String description, String color, String imageUrl) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.imageUrl = imageUrl;
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
