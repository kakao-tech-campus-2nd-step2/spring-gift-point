package gift.controller.api;

import gift.dto.product.ProductAddRequest;
import gift.dto.product.ProductResponse;
import gift.dto.product.ProductUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "상품 API")
public interface ProductApi {

    @Operation(summary = "새 상품을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "상품 등록 성공", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "상품 등록 실패(사유 : 카카오가 포함된 이름입니다.)", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<ProductResponse> addProduct(ProductAddRequest productAddRequest);

    @Operation(summary = "기존 상품을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "상품 수정 성공"),
            @ApiResponse(responseCode = "400", description = "상품 수정 실패(사유 : 카카오가 포함된 이름입니다.)"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류")
    })
    ResponseEntity<Void> updateProduct(Long id, ProductUpdateRequest productUpdateRequest);

    @Operation(summary = "특정 상품을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 상품 조회 성공", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<ProductResponse> getProduct(Long id);

    @Operation(summary = "모든 상품을 페이지 단위로 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 상품 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class)))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<List<ProductResponse>> getProducts(Long categoryId, Pageable pageable);

    @Operation(summary = "특정 상품을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청"),
            @ApiResponse(responseCode = "404", description = "상품 삭제 실패(사유 : 존재하지 않는 ID 입니다.)"),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류")
    })
    ResponseEntity<Void> deleteProduct(Long id);
}
