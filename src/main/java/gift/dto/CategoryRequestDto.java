package gift.dto;

public class CategoryRequestDto {
    private String color;
    private String description;
    private String imageUrl;
    private String name;

    public CategoryRequestDto(String color, String description, String imageUrl, String name) {
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
        this.name = name;
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
}
