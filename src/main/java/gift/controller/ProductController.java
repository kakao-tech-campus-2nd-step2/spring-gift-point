package gift.controller;

import gift.dto.ProductPageResponseDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "상품 관련 API")
public class ProductController {
    private final ProductService productService;
    private static final int DEFAULT_SIZE = 20;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "상품이 성공적으로 추가되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = ProductResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 예: 상품 이름이 15자를 초과하거나 유효하지 않은 특수 문자가 포함된 경우.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<ProductResponseDto> addProduct(
            @Parameter(description = "추가할 상품의 정보", required = true) @RequestBody ProductRequestDto productRequestDTO) {
        ProductResponseDto createdProductDTO = productService.addProduct(productRequestDTO);
        return new ResponseEntity<>(createdProductDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "상품 수정", description = "기존 상품을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품이 성공적으로 수정되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = ProductResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 예: 상품 이름이 15자를 초과하거나 유효하지 않은 특수 문자가 포함된 경우.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "상품 또는 카테고리를 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<ProductResponseDto> updateProduct(
            @Parameter(description = "수정할 상품 ID", required = true) @PathVariable Long id,
            @Parameter(description = "수정할 상품의 정보", required = true) @RequestBody ProductRequestDto productRequestDTO) {
        ProductResponseDto updatedProductDTO = productService.updateProduct(id, productRequestDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 목록이 성공적으로 반환되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = ProductPageResponseDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<ProductPageResponseDto> getAllProducts(
            @Parameter(description = "페이지 번호 (0부터 시작)", required = false, schema = @Schema(type = "integer", defaultValue = "0")) @RequestParam(defaultValue = "0") @Min(0) int page) {
        ProductPageResponseDto responseDto = productService.getAllProducts(page, DEFAULT_SIZE);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 조회", description = "ID로 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품이 성공적으로 반환되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = ProductResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<ProductResponseDto> getProductById(
            @Parameter(description = "조회할 상품 ID", required = true) @PathVariable Long id) {
        ProductResponseDto productResponseDto = productService.getProductById(id);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제", description = "기존 상품을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "상품이 성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "삭제할 상품 ID", required = true) @PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
