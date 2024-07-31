package gift.dto.request;

public class CategoryRequest {
    
    private String name;
    private String color;
    private String imageUrl;
    private String description;

    public CategoryRequest(String name, String color, String imageUrl, String description){
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

}
