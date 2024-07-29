package gift.dto;

import gift.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 DTO")
public class CategoryDTO {

    @Schema(description = "카테고리 ID", example = "1")
    @NotBlank(message = "아이디값은 필수 값입니다.")
    private int id;

    @Schema(description = "카테고리 이름", example = "교환권")
    @NotBlank(message = "이름은 필수 값입니다.")
    private String name;

    @Schema(description = "카테고리 색상", example = "#6c95d1")
    @NotBlank(message = "색상은 필수 값입니다.")
    private String color;

    @Schema(description = "이미지 URL", example = "https://example.com/image.png")
    @Size(max = 255, message = "이미지 URL은 최대 255자까지 가능합니다.")
    private String imageUrl;

    @Schema(description = "설명", example = "이 카테고리는 ...")
    @Size(max = 500, message = "설명은 최대 500자까지 가능합니다.")
    private String description;

    public CategoryDTO(int id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public static CategoryDTO convertDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
    }

    public int getId() {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
