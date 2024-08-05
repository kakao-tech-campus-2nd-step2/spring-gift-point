package gift.DTO;

public class CategoryResponseDTO {

    private String label;
    private String imageURL;
    private String title;
    private String description;
    private String backgroundColor;
    private Long categoryId;

    public CategoryResponseDTO(String label, String imageURL, String title, String description, String backgroundColor, Long categoryId) {
        this.label = label;
        this.imageURL = imageURL;
        this.title = title;
        this.description = description;
        this.backgroundColor = backgroundColor;
        this.categoryId = categoryId;
    }

    // Getter 및 Setter 메서드

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
