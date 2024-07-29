package gift.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "카테고리 응답 DTO")
public class CategoryResponseDTO {

    @Schema(description = "카테고리명")
    private String name;

    public String getName() {
        return name;
    }

    public CategoryResponseDTO() {
    }

    public CategoryResponseDTO(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CategoryResponseDTO categoryResponseDTO) {
            return Objects.equals(name, categoryResponseDTO.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

}
