package gift.product.docs;

import gift.product.dto.CategoryDTO;
import gift.product.model.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Category", description = "API 컨트롤러")
public interface CategoryControllerDocs {

    @Operation(summary = "카테고리 등록", description = "상품의 분류를 정하는 카테고리를 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "카테고리 등록 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Category.class))),
        @ApiResponse(responseCode = "400", description = "등록을 시도한 카테고리의 이름 누락"),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제 발생(인증 헤더 누락 또는 토큰 인증 실패)"),
        @ApiResponse(responseCode = "409", description = "등록을 시도한 카테고리 이름이 기등록된 카테고리의 이름과 중복")})
    ResponseEntity<?> registerCategory(CategoryDTO categoryDTO);

    @Operation(summary = "카테고리 이름 수정", description = "카테고리의 이름을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "카테고리 수정 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Category.class))),
        @ApiResponse(responseCode = "400", description = "수정을 시도한 카테고리의 이름 누락"),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제 발생(인증 헤더 누락 또는 토큰 인증 실패)"),
        @ApiResponse(responseCode = "404", description = "등록되지 않은 카테고리에 대한 수정 시도"),
        @ApiResponse(responseCode = "409", description = "수정을 시도한 카테고리 이름이 기등록된 카테고리의 이름과 중복(또는 기존과 동일한 이름으로 수정 시도)")})
    ResponseEntity<?> updateCategory(Long id, CategoryDTO categoryDTO);

    @Operation(summary = "카테고리 삭제", description = "상품의 분류를 정하는 카테고리를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제 발생(인증 헤더 누락 또는 토큰 인증 실패)"),
        @ApiResponse(responseCode = "404", description = "등록되지 않은 카테고리에 대한 삭제 시도")})
    ResponseEntity<Void> deleteCategory(Long id);
}
