package gift.administrator.product;

import gift.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Arrays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
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
@Tag(name = "product API", description = "product related API")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "get all products", description = "모든 상품을 조회합니다.")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "size", required = false, defaultValue = "10") int size,
        @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
        @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection) {
        size = PageUtil.validateSize(size);
        sortBy = PageUtil.validateSortBy(sortBy, Arrays.asList("id", "name"));
        Direction direction = PageUtil.validateDirection(sortDirection);
        Page<ProductDTO> productPage = productService.getAllProducts(page, size, sortBy,
            direction);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "get one product", description = "상품 아이디로 하나의 상품을 조회합니다.")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Long productId) {
        ProductDTO productDTO = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productDTO);
    }

    @PostMapping
    @Operation(summary = "add one product", description = "하나의 상품을 추가합니다. required info(name, price,"
        + " imageUrl, categoryId, option)")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        productService.existsByNameAddingProducts(productDTO);
        ProductDTO result = productService.addProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "update one product", description = "상품 아이디로 하나의 상품을 수정합니다. "
        + "required info(name, price, imageUrl, categoryId, option)")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("productId") Long productId,
        @Valid @RequestBody ProductDTO productDTO) {
        productService.existsByNameAndId(productDTO.getName(), productId);

        ProductDTO result = productService.updateProduct(productDTO, productId);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "delete one product", description = "상품 아이디로 하나의 상품을 삭제합니다.")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        productService.getProductById(productId);
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}
