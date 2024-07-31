package gift.doamin.category.dto;

import gift.doamin.category.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "카테고리 정보")
public class CategoryResponse {

    @Schema(description = "카테고리 id")
    private Long id;

    @Schema(description = "카테고리 이름")
    private String name;

    @Schema(description = "카테고리 색상")
    private String color;

    @Schema(description = "카테고리 이미지 경로")
    private String imageUrl;

    @Schema(description = "카테고리 설명")
    private String description;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.color = category.getColor();
        this.imageUrl = category.getImageUrl();
        this.description = category.getDescription();
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
