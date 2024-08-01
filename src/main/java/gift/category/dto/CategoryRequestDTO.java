package gift.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "카테고리 요청 DTO")
public class CategoryRequestDTO {

    @Schema(description = "카테고리명")
    private String name;

    public String getName() {
        return name;
    }

    public CategoryRequestDTO() {
    }

    public CategoryRequestDTO(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CategoryRequestDTO categoryRequestDTO) {
            return Objects.equals(name, categoryRequestDTO.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
