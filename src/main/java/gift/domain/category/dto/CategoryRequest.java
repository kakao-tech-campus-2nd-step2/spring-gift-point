package gift.domain.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 요청 DTO")
public class CategoryRequest {

    @Schema(description = "카테고리 이름", example = "카테고리 1")
    private String name;
    @Schema(description = "카테고리 색깔", example = "blue")
    private String color;

    @Schema(description = "카테고리 이미지 URL", example = "http://example.com/image.jpg")
    private String imageUrl;

    @Schema(description = "카테고리 설명", example = "설명")
    private String description;

    private CategoryRequest() {
    }

    public CategoryRequest(String name, String color, String imageUrl, String description) {
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
