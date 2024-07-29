package gift.product.docs;

import gift.product.dto.ProductDTO;
import gift.product.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

@Tag(name = "Product", description = "API 컨트롤러")
public interface ProductControllerDocs {

    @Operation(summary = "상품 목록", description = "등록된 상품 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "500", description = "사용자 요청에는 문제가 없으나, 서버가 이를 올바르게 처리하지 못한 경우")})
    Page<?> showProductList(Pageable pageable);

    @Operation(summary = "상품 등록", description = "상품을 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 등록 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "등록하고자 하는 상품의 정보를 제대로 입력하지 않은 경우"),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"),
        @ApiResponse(responseCode = "403", description = "상품 명에 \"카카오\"를 포함하여 상품의 등록을 시도한 경우(담당 MD와 협의한 경우에만 사용이 가능)"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리로 상품의 등록을 시도한 경우"),
        @ApiResponse(responseCode = "500", description = "상품 등록 정보에는 문제가 없으나, 서버가 이를 올바르게 처리하지 못한 경우")})
    ResponseEntity<?> registerProduct(ProductDTO productDTO, BindingResult bindingResult);

    @Operation(summary = "상품 수정", description = "기등록된 상품을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 수정 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "등록하고자 하는 상품의 정보를 제대로 입력하지 않은 경우"),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"),
        @ApiResponse(responseCode = "403", description = "상품 명에 \"카카오\"를 포함하여 상품의 수정을 시도한 경우(담당 MD와 협의한 경우에만 사용이 가능)"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리로 수정을 시도한 경우"),
        @ApiResponse(responseCode = "500", description = "상품 수정 정보에는 문제가 없으나, 서버가 이를 올바르게 처리하지 못한 경우")})
    ResponseEntity<?> updateProduct(Long id, ProductDTO productDTO, BindingResult bindingResult);

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품의 삭제를 시도한 경우"),
        @ApiResponse(responseCode = "500", description = "상품 삭제 요청은 올바르나, 서버가 이를 올바르게 처리하지 못한 경우")})
    ResponseEntity<Void> deleteProduct(Long id);

    @Operation(summary = "상품 키워드 조회", description = "상품 명에 특정 키워드가 포함된 상품 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 명에 특정 키워드가 포함된 상품 목록 조회 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"),
        @ApiResponse(responseCode = "500", description = "사용자의 요청은 올바르나, 서버가 이를 올바르게 처리하지 못한 경우")})
    Page<?> searchProduct(String keyword, Pageable pageable);
}
