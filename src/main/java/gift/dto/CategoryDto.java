package gift.dto;

public class CategoryDto {
    private final Long id;
    private final String name;
    private final String color;
    private final String imgUrl;
    private final String description;

    public CategoryDto(Long id, String name, String color, String imgUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imgUrl = imgUrl;
        this.description = description;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDescription() {
        return description;
    }
}