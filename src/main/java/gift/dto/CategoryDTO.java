package gift.dto;

public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String color;
    private String imageUrl;


    public CategoryDTO() {}

    public CategoryDTO(Long id, String name, String description, String color, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
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
