package gift.dto;

public class CategoryRequestDto {
    private final Long id;
    private final String name;
    private final String color;
    private final String description;
    private final String imageUrl;

    public CategoryRequestDto(Long id, String name, String color, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
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

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
