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
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping
    public List<ProductDTO> getProducts() {
        return productService.findAllProducts();
    }

    @Operation(summary = "제품 옵션 조회", description = "특정 제품의 옵션을 조회합니다.")
    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionDTO>> getOptions(@PathVariable Long productId) {
        List<OptionDTO> options = optionService.getOptionsByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "제품 옵션 추가", description = "특정 제품에 새로운 옵션을 추가합니다.")
    @PostMapping("/{productId}/options")
    public ResponseEntity<OptionDTO> addOption(@PathVariable Long productId,
        @Valid @RequestBody OptionDTO optionDTO) {
        OptionDTO savedOption = optionService.addOption(productId, optionDTO);
        return ResponseEntity.ok(savedOption);
    }

    @Operation(summary = "페이지 단위 제품 조회", description = "페이지 단위로 제품을 조회합니다.")
    @GetMapping("/paged")
    public Page<ProductDTO> getProducts(Pageable pageable) {
        return productService.findAllProducts(pageable);
    }

    @Operation(summary = "제품 생성", description = "새로운 제품을 생성합니다.")
    @PostMapping
    public ProductDTO createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    @Operation(summary = "제품 수정", description = "특정 제품을 수정합니다.")
    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
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
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
