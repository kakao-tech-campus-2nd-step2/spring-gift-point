package gift.product;

import gift.common.model.PageResponseDto;
import gift.product.model.ProductRequest;
import gift.product.model.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
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

@Tag(name = "Product API", description = "Product 를 관리하는 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "전체 Product 조회", description = "전체 Product 를 조회합니다.")
    @GetMapping
    public ResponseEntity<PageResponseDto<ProductResponse>> getAllProducts(
        @PageableDefault(size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(
            PageResponseDto.of(productService.getAllProducts(pageable).getContent(), pageable));
    }

    @Operation(summary = "특정 Product 조희", description = "id에 해당하는 Product 를 조회합니다.")
    @Parameter(name = "id", description = "조회할 product 의 id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Product 추가", description = "Product 를 생성합니다. (Option 을 하나라도 가지고 있어야 합니다.)")
    @PostMapping
    public ResponseEntity<Void> addProduct(
        @Valid @RequestBody ProductRequest.Create productCreate) {
        Long productId = productService.insertProduct(productCreate);
        return ResponseEntity.created(URI.create("/api/products/" + productId)).build();
    }

    @Operation(summary = "특정 Product 수정", description = "id에 해당하는 Product 를 수정합니다.")
    @Parameter(name = "id", description = "수정할 product 의 id")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
        @Valid @RequestBody ProductRequest.Update productUpdate,
        @PathVariable("id") Long id) {
        productService.updateProductById(id, productUpdate);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "특정 Product 삭제", description = "id에 해당하는 Product 를 삭제합니다.")
    @Parameter(name = "id", description = "삭제할 product 의 id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok().build();
    }

}
