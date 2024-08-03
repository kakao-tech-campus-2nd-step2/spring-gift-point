package gift.domain.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "전체 카테고리 조회 응답 Dto")
public record CategoriesResponse(
    @Schema(description = "카테고리 리스트")
    List<CategoryResponse> categories
) {

}
