package gift.controller;

import gift.dto.CategoryDTO;
import gift.dto.OptionDTO;
import gift.dto.ProductDTO;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "제품 API", description = "제품 관련된 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public ProductController(ProductService productService,
        CategoryService categoryService, OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    @Operation(summary = "제품 목록 조회", description = "모든 제품을 조회합니다.")
    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "카테고리별 또는 전체 제품 조회", description = "카테고리별 또는 전체 제품을 페이지 단위로 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProducts(
        @RequestParam(required = false) Long categoryId,
        Pageable pageable) {
        Page<ProductDTO> products;
        if (categoryId != null) {
            products = productService.findProductsByCategory(categoryId, pageable);
        } else {
            products = productService.findAllProducts(pageable);
        }
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "특정 제품 조회", description = "특정 제품의 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        ProductDTO productDTO = productService.getProductById(productId);
        return ResponseEntity.ok(productDTO);
    }

    @Operation(summary = "제품 옵션 조회", description = "특정 제품의 옵션을 조회합니다.")
    @GetMapping("/{productId}/options")
    public ResponseEntity<Map<String, Object>> getOptions(@PathVariable Long productId) {
        List<OptionDTO> options = optionService.getOptionsByProductId(productId);
        Map<String, Object> response = new HashMap<>();
        response.put("options", options);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "제품 옵션 추가", description = "특정 제품에 새로운 옵션을 추가합니다.")
    @PostMapping("/{productId}/options")
    public ResponseEntity<OptionDTO> addOption(@PathVariable Long productId,
        @Valid @RequestBody OptionDTO optionDTO) {
        OptionDTO savedOption = optionService.addOption(productId, optionDTO);
        return ResponseEntity.ok(savedOption);
    }

    @Operation(summary = "제품 생성", description = "새로운 제품을 생성합니다.")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @Operation(summary = "제품 수정", description = "특정 제품을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "제품 카테고리 수정", description = "특정 제품의 카테고리를 수정합니다.")
    @PutMapping("/{id}/category")
    public ResponseEntity<ProductDTO> updateProductCategory(@PathVariable Long id,
        @RequestBody CategoryDTO categoryDTO) {
        ProductDTO updatedProduct = productService.updateProductCategory(id, categoryDTO.getName());
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "제품 삭제", description = "특정 제품을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
