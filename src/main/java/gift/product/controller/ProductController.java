package gift.product.controller;

import gift.config.PageConfig;
import gift.product.dto.request.CreateProductRequest;
import gift.product.dto.request.UpdateProductRequest;
import gift.product.dto.response.ProductResponse;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/products")
@Tag(name = "상품 관리", description = "상품을 관리하는 API")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "모든 상품 조회", description = "모든 상품을 페이지네이션으로 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "상품이 존재하지 않음")
    })
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
        @PageableDefault(
            size = PageConfig.PAGE_PER_COUNT,
            sort = PageConfig.SORT_STANDARD,
            direction = Direction.DESC
        ) Pageable pageable) {
        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("{id}")
    @Operation(summary = "특정 상품 조회", description = "id를 통해 특정 상품을 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "해당 id를 가진 상품 없음")
    })
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "성공", headers = {
            @Header(name = "location", description = "상품 생성 위치 엔드포인트")
        }),
        @ApiResponse(responseCode = "409", description = "동일한 상품이 이미 존재함")
    })
    public ResponseEntity<ProductResponse> addProduct(
        @RequestBody @Valid CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        URI location = UriComponentsBuilder.fromPath("/api/products/{id}")
            .buildAndExpand(response.id())
            .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "상품 수정", description = "{id}를 가진 상품을 수정합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "해당 id를 가진 상품 없음")
    })
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody @Valid
    UpdateProductRequest request) {
        productService.updateProduct(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    @Operation(summary = "상품 삭제", description = "{id}를 가진 상품 삭제")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "해당 id를 가진 상품 없음")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}
