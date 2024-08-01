package gift.controller;

import gift.dto.ProductDTO;
import gift.model.Product;

import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@Tag(name = "상품 관리 API", description = "상품 관리를 위한 API")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    public Product addProduct(@RequestBody ProductDTO newProductDTO) {
        productService.saveProduct(newProductDTO);
        return ProductService.toEntity(newProductDTO, null,
            categoryService.findCategoryById(newProductDTO.categoryId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 얻기", description = "ID로 상품을 조회합니다.")
    public Product getProduct(@PathVariable(value = "id") long id) {
        return productService.findProductsById(id);
    }

    @GetMapping
    @Operation(summary = "모든 상품 얻기", description = "모든 상품을 조회합니다.")
    public Page<Product> getAllProduct(@PageableDefault(size = 5) Pageable pageable) {
        return productService.findAllProducts(pageable);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    public void deleteProduct(@PathVariable(value = "id") long id) {
        productService.deleteProductAndWishlistAndOptions(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    public Product updateProduct(@PathVariable("id") long id,
        @RequestBody ProductDTO updatedProductDTO) {
        productService.updateProduct(updatedProductDTO, id);
        return productService.findProductsById(id);
    }
}
