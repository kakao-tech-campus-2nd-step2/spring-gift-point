package gift.domain.product.controller;

import gift.domain.option.dto.OptionRequest;
import gift.domain.option.dto.OptionResponse;
import gift.domain.option.service.OptionService;
import gift.domain.product.dto.ProductCreateResponse;
import gift.domain.product.dto.ProductRequest;
import gift.domain.product.dto.ProductResponse;
import gift.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/products")
@Tag(name = "ProductController", description = "Product API")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping()
    @Operation(summary = "상품 전체 조회", description = "상품 전체를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameters({
        @Parameter(name = "pageNo", description = "페이지 번호 (0부터 시작)", example = "0"),
        @Parameter(name = "pageSize", description = "페이지 크기", example = "10")
    })
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<ProductResponse> response = productService.getAllProducts(pageNo, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 상품 조회", description = "id 값의 상품을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameter(name = "id", description = "조회할 상품의 ID", example = "1")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long id) {
        ProductResponse response = productService.getProduct(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/options")
    @Operation(summary = "해당 상품에 대한 옵션들 전체 조회", description = "id 값으로 지정된 상품에 대한 옵션들을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameter(name = "id", description = "옵션을 조회할 상품의 ID", example = "1")
    public ResponseEntity<List<OptionResponse>> getProductOptions(@PathVariable("id") Long id) {
        List<OptionResponse> response = optionService.getProductOptions(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    @Operation(summary = "상품 생성", description = "관리자가 상품을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<ProductCreateResponse> createProduct(
        @RequestBody @Valid ProductRequest productRequest) {
        ProductCreateResponse response = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @PostMapping("/{id}/options")
    @Operation(summary = "해당 상품에 대한 옵션 추가", description = "id 값으로 지정된 상품에 대한 옵션을 추가합니다.")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameter(name = "id", description = "옵션을 추가할 상품의 ID", example = "1")
    public ResponseEntity<OptionResponse> addOptionToProduct(@PathVariable("id") Long id,
        @Valid @RequestBody OptionRequest request) {
        OptionResponse response = optionService.addOptionToProduct(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }


    @PutMapping("/{id}")
    @Operation(summary = "상품 수정", description = "해당 상품을 수정합니다.")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameter(name = "id", description = "수정할 상품의 ID", example = "1")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("id") Long id,
        @RequestBody @Valid ProductRequest productRequest) {
        ProductResponse response = productService.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제", description = "해당 상품을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.")
    @Parameter(name = "id", description = "삭제할 상품의 ID", example = "1")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
