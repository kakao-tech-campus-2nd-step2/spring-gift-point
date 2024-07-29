package gift.administrator.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "카테고리 DTO")
public class CategoryDTO {

    private Long id;
    @Schema(description = "카테고리 이름", example = "상품권")
    @NotBlank(message = "카테고리 이름을 입력하지 않았습니다.")
    private String name;
    @Schema(description = "카테고리 색상", example = "#ff11ff")
    @Pattern(regexp = "(^#(([0-9a-fA-F]{2}){3}|([0-9a-fA-F]){3})$)"
        + "|(^#[0-9a-fA-F]{8}$|#[0-9a-fA-F]{6}$|#[0-9a-fA-F]{4}$|#[0-9a-fA-F]{3}$)"
        , message = "컬러코드가 아닙니다.")
    private String color;
    @Schema(description = "카테고리 이미지 링크", example = "image.url")
    private String imageUrl;
    @Schema(description = "카테고리 설명", example = "환불이 불가합니다.")
    private String description;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public CategoryDTO(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor(){return color;}

    public String getImageUrl(){return imageUrl;}

    public String getDescription(){return description;}

    public Category toCategory() {
        return new Category(id, name, color, imageUrl, description);
    }

    public static CategoryDTO fromCategory(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getColor(),
            category.getImageUrl(), category.getDescription());
    }
}
