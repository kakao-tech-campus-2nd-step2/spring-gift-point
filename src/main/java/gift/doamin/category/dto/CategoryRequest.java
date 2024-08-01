package gift.doamin.category.dto;

import gift.doamin.category.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "카테고리 등록, 수정 요청")
public class CategoryRequest {

    @Schema(description = "카테고리 이름")
    @NotBlank
    private String name;

    @Schema(description = "카테고리 색상")
    @NotBlank
    private String color;

    @Schema(description = "카테고리 이미지 경로")
    @NotBlank
    private String imageUrl;

    @Schema(description = "카테고리 설명")
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

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public Category toEntity() {
        return new Category(name, color, imageUrl, description);
    }
}
