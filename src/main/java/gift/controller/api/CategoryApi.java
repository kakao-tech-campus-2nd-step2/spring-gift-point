package gift.controller.api;

import gift.dto.category.CategoryRequest;
import gift.dto.category.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "카테고리 API")
public interface CategoryApi {

    @Operation(summary = "새 카테고리를 등록한다.")
    ResponseEntity<Void> addCategory(CategoryRequest categoryRequest);

    @Operation(summary = "기존 카테고리를 수정한다.")
    ResponseEntity<Void> updateCategory(Long id, CategoryRequest categoryRequest);

    @Operation(summary = "특정 카테고리를 조회한다.")
    ResponseEntity<CategoryResponse> getCategory(Long id);

    @Operation(summary = "모든 카테고리를 페이지 단위로 조회한다.")
    ResponseEntity<List<CategoryResponse>> getCategories(Pageable pageable);

    @Operation(summary = "특정 카테고리를 삭제한다.")
    ResponseEntity<Void> deleteCategory(Long id);
}
