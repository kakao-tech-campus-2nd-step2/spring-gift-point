package gift.dto.request;


import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {

    @NotBlank(message = "카테고리명은 필수 항목입니다.")
    private String name;

    @NotBlank(message = "컬러명은 필수 항목입니다.")
    private String color;

    private String imageUrl;

    private String description;

    public CategoryRequest(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
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
}
