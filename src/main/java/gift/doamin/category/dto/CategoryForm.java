package gift.doamin.category.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "카테고리 등록, 수정 요청")
public class CategoryForm {

    @Schema(description = "카테고리 id")
    private Long id;

    @Schema(description = "카테고리 이름")
    @NotBlank
    private String name;

    @JsonCreator
    public CategoryForm(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
