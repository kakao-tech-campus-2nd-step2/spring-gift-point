package gift.controller.api;

import gift.dto.category.CategoryRequest;
import gift.dto.category.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "카테고리 API")
public interface CategoryApi {
    @Operation(summary = "새 카테고리를 생성한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "카테고리 생성 성공"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청"),
            @ApiResponse(responseCode = "409", description = "카테고리 생성 실패(사유 : 이미 존재하는 이름입니다. )"),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류")
    })
    ResponseEntity<Void> addCategory(CategoryRequest categoryRequest);

    @Operation(summary = "기존 카테고리를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "카테고리 수정 성공"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청"),
            @ApiResponse(responseCode = "409", description = "카테고리 수정 실패(사유 : 이미 존재하는 이름입니다. )"),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류")
    })
    ResponseEntity<Void> updateCategory(Long id, CategoryRequest categoryRequest);

    @Operation(summary = "특정 카테고리를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 카테고리 조회 성공", content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<CategoryResponse> getCategory(Long id);

    @Operation(summary = "모든 카테고리를 페이지 단위로 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 카테고리 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class)))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<List<CategoryResponse>> getCategories();

    @Operation(summary = "특정 카테고리를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청"),
            @ApiResponse(responseCode = "404", description = "카테고리 삭제 실패(사유 : 존재하지 않는 ID 입니다.)"),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류")
    })
    ResponseEntity<Void> deleteCategory(Long id);
}
