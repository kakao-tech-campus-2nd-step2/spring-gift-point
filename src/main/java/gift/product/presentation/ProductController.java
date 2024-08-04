package gift.product.presentation;

import gift.exception.InvalidProduct;
import gift.exception.NotFoundOption;
import gift.product.application.dto.ProductRequestDto;
import gift.product.application.dto.ProductResponseDto;
import gift.product.application.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<ProductResponseDto> getAllProducts(@RequestParam Long categoryId, Pageable pageable) {
        return productService.getAllProducts(categoryId, pageable);
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProduct(@PathVariable Long id) {
        return productService.getProductById(id)
            .orElseThrow(() -> new InvalidProduct("유효하지 않은 상품입니다"));
    }

    @PostMapping
    public ProductResponseDto addProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
        return productService.postProduct(productRequestDto);
    }

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto productRequestDto) {
        return productService.putProduct(id, productRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @PutMapping("/category/{id}")
    public ProductResponseDto changeCategory(@PathVariable(name="id") Long productID, @RequestParam Long categoryId) {
        return productService.putCategory(productID, categoryId);
    }

    @GetMapping("/options/{optionId}")
    public Long getProductOfOption(@PathVariable Long optionId) throws NotFoundOption {
        return productService.findPrdouctOfOption(optionId);
    }

}
