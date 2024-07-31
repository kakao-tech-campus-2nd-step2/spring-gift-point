package gift.controller;

import gift.dto.CategoryResponse;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.dto.ProductResponseWithoutCategoryId;
import gift.dto.ProductUpdateRequest;
import gift.dto.ProductUpdateResponse;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/products")
@Tag(name = "Product API", description = "상품 관련 API")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회 (카테고리 별)", description = "특정 카테고리에 속한 모든 상품을 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공")
    })
    public ResponseEntity<Map<String, Object>> getProductsByCategory(
        @RequestParam(required = false) Long categoryId,
        @RequestParam(defaultValue = "name,asc") String[] sort) {

        CategoryResponse categoryResponse = productService.getCategoryById(categoryId);
        List<ProductResponseWithoutCategoryId> products = productService.getProductsByCategory(categoryId, sort);

        Map<String, Object> response = new HashMap<>();
        response.put("category", categoryResponse);
        response.put("products", products);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회", description = "특정 상품의 정보를 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "-40401", description = "해당하는 상품이 존재하지 않음")
    })
    public ResponseEntity<Map<String, ProductResponse>> getProduct(@PathVariable Long productId) {
        ProductResponse productResponse = ProductResponse.from(productService.findById(productId));
        Map<String, ProductResponse> response = new HashMap<>();
        response.put("product", productResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "상품 생성", description = "새 상품을 등록한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 추가 성공"),
        @ApiResponse(responseCode = "-40001", description = "유효성 검사 실패"),
        @ApiResponse(responseCode = "-40002", description = "상품 등록 시 옵션이 하나 이상 주어지지 않음"),
        @ApiResponse(responseCode = "-40402", description = "카테고리를 찾기 실패"),
        @ApiResponse(responseCode = "-40903", description = "등록할 상품이 이미 존재함"),
        @ApiResponse(responseCode = "-40904", description = "상품 옵션에 중복되는 이름의 옵션이 있음")
    })
    public ResponseEntity<Map<String, ProductResponse>> addProduct(
        @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.addProduct(productRequest);
        Map<String, ProductResponse> response = new HashMap<>();
        response.put("product", productResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "기존 상품의 정보를 수정한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 수정 성공"),
        @ApiResponse(responseCode = "-40001", description = "유효성 검사 실패"),
        @ApiResponse(responseCode = "-40401", description = "ID에 해당하는 상품이 존재하지 않음"),
        @ApiResponse(responseCode = "-40402", description = "카테고리를 찾기 실패"),
        @ApiResponse(responseCode = "-40903", description = "등록할 상품이 이미 존재함")
    })
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId,
        @Valid @RequestBody ProductUpdateRequest updatedProductRequest) {
        productService.updateProduct(productId, updatedProductRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "특정 상품을 삭제한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
        @ApiResponse(responseCode = "-40401", description = "해당하는 상품이 존재하지 않음")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteById(productId);
        return ResponseEntity.noContent().build();
    }
}
