package gift.Model;

public class CategoryDto {

    private long id;
    private String name;
    private String description;
    private String color;
    private String imageUrl;

    public CategoryDto() {

    }

    public CategoryDto(long id, String name, String description, String color, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
