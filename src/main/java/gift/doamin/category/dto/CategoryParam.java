package gift.doamin.category.dto;

import gift.doamin.category.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 정보")
public class CategoryParam {

    @Schema(description = "카테고리 id")
    private Long id;

    @Schema(description = "카테고리 이름")
    private String name;

    public CategoryParam(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
