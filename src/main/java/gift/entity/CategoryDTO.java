package gift.entity;


import io.swagger.v3.oas.annotations.media.Schema;

public class CategoryDTO {
    @Schema(description = "카테고리명", nullable = false, example = "전자기기")
    private String name;
    @Schema(description = "카테고리 색상", nullable = false, example = "#FFFFFF")
    private String color;
    @Schema(description = "카테고리 이미지 url", nullable = false, example = "https://www.test.com")
    private String imageurl;
    @Schema(description = "카테고리 설명", nullable = false, example = "카테고리 설명 입니다")
    private String description;

    public CategoryDTO() {
    }

    public CategoryDTO(String name, String color, String imageurl, String description) {
        this.name = name;
        this.color = color;
        this.imageurl = imageurl;
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

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
