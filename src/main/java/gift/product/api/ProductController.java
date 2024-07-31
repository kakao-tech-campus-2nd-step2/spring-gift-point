package gift.product.api;

import gift.product.application.OptionService;
import gift.product.application.ProductService;
import gift.product.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService,
                             OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    // 상품 전체 조회
    @GetMapping
    public Page<GetProductResponse> getPagedProducts(
            @PageableDefault(
                    sort = "id",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return productService.getPagedProducts(pageable);
    }

    // 상품 상세 조회
    @GetMapping("/{id}")
    public GetProductResponse getProduct(@PathVariable("id") Long id) {
        return productService.getProductByIdOrThrow(id);
    }

    // 상품 추가
    @PostMapping
    public ProductResponse addProduct(@RequestBody @Valid ProductRequest request) {
        return productService.createProduct(request);
    }

    // 상품 하나 삭제
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
    }

    // 상품 전체 삭제
    @DeleteMapping
    public void deleteAllProducts() {
        productService.deleteAllProducts();
    }

    // 상품 수정
    @PutMapping("/{id}")
    public void updateProduct(@PathVariable("id") Long id,
                              @RequestBody @Valid ProductRequest request) {
        productService.updateProduct(id, request);
    }

    // 상품 옵션 조회
    @GetMapping("/{id}/options")
    public List<OptionResponse> getProductOptions(@PathVariable("id") Long id) {
        return optionService.getOptionsByProductId(id);
    }

    // 상품 옵션 추가
    @PostMapping ("/{id}/options")
    public OptionResponse addOptionToProduct(@PathVariable("id") Long id,
                                             @RequestBody @Valid OptionRequest request) {
        return optionService.addOptionToProduct(id, request);
    }

    // 상품 옵션 삭제
    @DeleteMapping("/{productId}/options/{optionId}")
    public void deleteOptionFromProduct(@PathVariable("productId") Long productId,
                                        @PathVariable("optionId") Long optionId) {
        optionService.deleteOptionFromProductById(productId, optionId);
    }

    // 상품 옵션 수정
    @PutMapping("/{productId}/options/{optionId}")
    public OptionResponse updateOption(@PathVariable("productId") Long productId,
                                       @PathVariable("optionId") Long optionId,
                                       @RequestBody @Valid OptionRequest request) {
        return optionService.updateOptionById(optionId, request);
    }
}
