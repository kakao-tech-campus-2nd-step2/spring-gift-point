package gift.product.controller.product;

import gift.product.dto.product.ClientProductRequest;
import gift.product.dto.product.ClientProductUpdateRequest;
import gift.product.dto.product.PageProductResponse;
import gift.product.dto.product.ProductResponse;
import gift.product.exception.ExceptionResponse;
import gift.product.model.Product;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

@Tag(name = "product", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageProductResponse.class)))
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProductAll(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size,
        @RequestParam(name = "sort", defaultValue = "name,asc") String sortParam,
        @RequestParam(name = "categoryId", required = false) Long categoryId) {

        String[] sortParamSplited = sortParam.split(",");
        String sort = sortParamSplited[0];
        String direction = (sortParamSplited.length > 1) ? sortParamSplited[1] : "asc";

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);

        if (categoryId == null) {
            return ResponseEntity.ok(productService.getProductAll(pageable));
        }

        return ResponseEntity.ok(productService.getProductAll(pageable, categoryId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 등록 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Void> insertProduct(@Valid @RequestBody ClientProductRequest clientProductRequest) {
        productService.insertProduct(clientProductRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable(name = "id") Long id,
        @Valid @RequestBody ClientProductUpdateRequest clientProductUpdateRequest) {
        productService.updateProduct(id, clientProductUpdateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "상품 삭제 실패 (존재하지 않는 ID)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
