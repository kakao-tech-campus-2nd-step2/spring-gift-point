package gift.category;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 수신용 DTO")
public class CategoryDTO {

    @Schema(description = "카테고리 명")
    private String name;

    public String getName() {
        return name;
    }

    public CategoryDTO() {
    }

    public CategoryDTO(String name) {
        this.name = name;
    }

}
