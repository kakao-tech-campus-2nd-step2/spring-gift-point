package gift.model.dto;

import gift.model.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "카테고리 데이터 전송 객체")
public class CategoryDTO {

    @Schema(description = "카테고리 ID(카테고리 추가 시 입력 불필요)", example = "1")
    private final Long categoryId;

    @Schema(description = "카테고리 이름", example = "전자기기")
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Pattern(regexp = "[a-zA-Z0-9가-힣() +\\-&\\[\\]/_]*", message = "(),[],+,-,&,/,_ 를 제외한 특수문자는 사용이 불가합니다.")
    private final String name;

    @Schema(description = "카테고리 이미지 URL", example = "https://example.com/images/electronics.jpg")
    private final String imageUrl;

    @Schema(description = "카테고리 설명", example = "전자기기 입니다.")
    private final String description;

    public CategoryDTO(Long categoryId, String name, String imageUrl, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public CategoryDTO(Category category) {
        this.categoryId = category.getId();
        this.name = category.getName();
        this.imageUrl = category.getImgUrl();
        this.description = category.getDescription();
    }

    public CategoryDTO(String name, String imageUrl, String description) {
        this.categoryId = null;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }
    public Long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }


    public Category toEntity() {
        return new Category(categoryId, name, imageUrl, description);
    }
}
