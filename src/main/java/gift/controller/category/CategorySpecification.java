package gift.controller.category;

import gift.dto.category.CategoryRequest;
import gift.dto.category.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "카테고리 관리", description = "상품 카테고리 관리를 위한 API")
public interface CategorySpecification {

    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "카테고리 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryResponse.class)
                            )
                    )
            })
    ResponseEntity<CategoryResponse.InfoList> getAllCategories();

    @Operation(summary = "카테고리 조회", description = "주어진 ID에 해당하는 카테고리를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "카테고리 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
            })
    ResponseEntity<CategoryResponse.Info> getCategory(@Parameter(description = "조회할 카테고리의 ID") @PathVariable(name = "id") Long categoryId);

    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "카테고리 추가 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "{\"message\":\"카테고리가 추가되었습니다!\"}")
                            )
                    )
            })
    ResponseEntity<String> addCategory(@RequestBody CategoryRequest categoryRequest);

    @Operation(summary = "카테고리 수정", description = "주어진 ID에 해당하는 카테고리를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "카테고리 수정 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "{\"message\":\"카테고리가 수정되었습니다!\"}")
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
            })
    ResponseEntity<String> updateCategory(@Parameter(description = "수정할 카테고리의 ID") @PathVariable(name = "id") Long categoryId,
                                          @RequestBody CategoryRequest categoryRequest);

    @Operation(summary = "카테고리 삭제", description = "주어진 ID에 해당하는 카테고리를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
            })
    ResponseEntity<String> deleteCategory(@Parameter(description = "삭제할 카테고리의 ID") @PathVariable(name = "id") Long categoryId);
}
