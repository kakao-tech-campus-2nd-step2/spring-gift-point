package gift.controller;

import gift.dto.ProductCreateRequest;
import gift.dto.ProductRequest;
import gift.dto.ResponseMessage;
import gift.entity.Product;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "상품 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))))
    public Page<Product> getProduct(
        @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "ID로 상품 조회", description = "상품 ID에 해당하는 상품을 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "상품 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))))
    public Product getOneProduct(@PathVariable("productId") Long productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.",
        responses = @ApiResponse(responseCode = "201", description = "상품 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))))
    public ResponseEntity<Product> addProduct(
        @Valid @RequestBody ProductCreateRequest productCreateRequest) {
        Product product = productService.saveProduct(productCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 업데이트", description = "상품 ID에 해당하는 상품을 업데이트합니다.",
        responses = @ApiResponse(responseCode = "200", description = "상품 업데이트 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))))
    public ResponseEntity<Product> changeProduct(@PathVariable("productId") Long productId,
        @Valid @RequestBody ProductRequest productRequest) {
        Product product = productService.updateProduct(productId, productRequest);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "상품 ID에 해당하는 상품을 삭제합니다.",
        responses = @ApiResponse(responseCode = "200", description = "상품 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))))
    public ResponseEntity<ResponseMessage> removeProduct(
        @PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        ResponseMessage responseMessage = new ResponseMessage("삭제되었습니다.");
        return ResponseEntity.ok(responseMessage);
    }
}
