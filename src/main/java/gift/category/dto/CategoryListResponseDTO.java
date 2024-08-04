package gift.category.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "카테고리 리스트 응답 DTO")
public class CategoryListResponseDTO {

    @Schema(description = "카테고리 리스트")
    private List<CategoryResponseDTO> categories;

    public CategoryListResponseDTO() {
    }

    public CategoryListResponseDTO(List<CategoryResponseDTO> categories) {
        this.categories = categories;
    }

    public List<CategoryResponseDTO> getCategories() {
        return categories;
    }
}
