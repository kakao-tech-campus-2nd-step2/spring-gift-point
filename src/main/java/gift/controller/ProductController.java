package gift.controller;

import gift.dto.*;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;


    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @Operation(summary = "상품 등록 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<SuccessResponse> saveProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        productService.addProduct(requestDto);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "상품이 성공적으로 등록되었습니다."));
    }

    @Operation(summary = "상품 전체 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAll(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNum,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size,
            @RequestParam(required = false, defaultValue = "id", value = "criteria") String criteria
    ) {
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.ASC, criteria));
        List<ProductResponseDto> responses = productService.findAll(pageable);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "상품 개별 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("id") Long id) {
        ProductResponseDto response = productService.findProduct(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 수정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> editProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductChangeRequestDto request) {
        ProductResponseDto edited = productService.editProduct(id, request);
        return ResponseEntity.ok(edited);
    }

    @Operation(summary = "상품 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "상품 옵션 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 옵션 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionResponseDto>> getOptions(@PathVariable("productId") Long productId) {
        var result = optionService.getOptions(productId);
        return ResponseEntity.ok(result);
    }
}
