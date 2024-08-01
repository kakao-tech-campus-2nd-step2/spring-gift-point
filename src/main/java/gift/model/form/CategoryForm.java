package gift.model.form;

import gift.model.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "카테고리 입력 폼 객체")
public class CategoryForm {

    @Schema(description = "카테고리 ID(카테고리 추가 시 입력 불필요)", example = "1")
    private Long id;

    @Schema(description = "카테고리 이름", example = "전자기기")
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Pattern(regexp = "[a-zA-Z0-9가-힣() +\\-&\\[\\]/_]*", message = "(),[],+,-,&,/,_ 를 제외한 특수문자는 사용이 불가합니다.")
    private String name;

    @Schema(description = "카테고리 이미지 URL", example = "https://example.com/images/electronics.jpg")
    private String imgUrl;

    @Schema(description = "카테고리 설명", example = "전자기기 입니다.")
    private String description;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public CategoryForm(Long id, String name, String imgUrl, String description) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.description = description;
    }

    public CategoryForm() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category toEntity() {
        return new Category(id, name, imgUrl, description);
    }
}
