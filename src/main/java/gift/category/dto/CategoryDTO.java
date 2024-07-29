package gift.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

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

    @Override
    public boolean equals(Object object) {
        if (object instanceof CategoryDTO categoryDTO) {
            return Objects.equals(name, categoryDTO.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
