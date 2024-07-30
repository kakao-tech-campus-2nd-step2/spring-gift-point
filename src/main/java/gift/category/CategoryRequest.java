package gift.category;

public class CategoryRequest {
    private Long id;

    private String name;

    private String color;

    private String imageUrl;

    private String description;

    protected CategoryRequest(){
    }

    public CategoryRequest(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Category toEntity(){
        return new Category(this.name, this.color, this.imageUrl, this.description);
    }
}
