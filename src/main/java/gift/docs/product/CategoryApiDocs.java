package gift.docs.product;

import gift.product.presentation.dto.CategoryRequest;
import gift.product.presentation.dto.ResponseCategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Category", description = "카테고리 관련 API")
public interface CategoryApiDocs {

    @Operation(summary = "카테고리 목록 조회")
    public ResponseEntity<List<ResponseCategoryDto>> getCategories();

    @Operation(summary = "카테고리 생성")
    public ResponseEntity<Void> createCategory(
        List<CategoryRequest.Create> requestCategoryDto);

    @Operation(summary = "카테고리 수정")
    public ResponseEntity<Long> updateCategory(
        Long id,
        CategoryRequest.Update requestCategoryDto);

    @Operation(summary = "카테고리 삭제")
    public ResponseEntity<Long> deleteCategory(
        Long id);

}
