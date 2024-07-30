package gift.controller;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.entity.Product;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    @Operation(summary = "상품 목록 조회 (페이지네이션 적용)", description = "모든 상품의 목록을 페이지 단위로 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = ProductResponse.class))})
    })
    public ResponseEntity<Slice<ProductResponse>> getPagedProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "name,asc") String[] sort,
        @RequestParam(required = false) Long categoryId) {
        Sort sorting = Sort.by(Sort.Order.by(sort[0]).with(Sort.Direction.fromString(sort[1])));
        Pageable pageable = PageRequest.of(page, size, sorting);
        Slice<ProductResponse> productsPage = productService.findAll(pageable, categoryId);
        return ResponseEntity.ok(productsPage);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회", description = "특정 상품의 정보를 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = ProductResponse.class))}),
        @ApiResponse(responseCode = "404", description = "ID에 해당하는 상품이 존재하지 않습니다.")
    })
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        Optional<Product> product = productService.findById(productId);
        if (product.isPresent()) {
            ProductResponse productResponse = ProductResponse.from(product.get());
            return ResponseEntity.ok(productResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @Operation(summary = "상품 생성", description = "새 상품을 등록한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 추가 성공",
            content = {@Content(schema = @Schema(implementation = ProductResponse.class))}),
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
        response.put("created_product", productResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "기존 상품의 정보를 수정한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = ProductResponse.class))}),
        @ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
        @ApiResponse(responseCode = "404", description = "ID에 해당하는 상품이 존재하지 않음")
    })
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId,
        @Valid @RequestBody ProductRequest updatedProductRequest) {
        ProductResponse productResponse = productService.updateProduct(productId, updatedProductRequest);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "특정 상품을 삭제한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "ID에 해당하는 상품이 존재하지 않음")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        if (!productService.findById(productId).isPresent()) {
            return ResponseEntity.noContent().build();
        }
        productService.deleteById(productId);
        return ResponseEntity.ok().build();
    }
}
