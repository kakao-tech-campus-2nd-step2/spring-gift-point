package gift.domain.controller;

import gift.domain.controller.apiResponse.OptionAddApiResponse;
import gift.domain.controller.apiResponse.ProductAddApiResponse;
import gift.domain.controller.apiResponse.ProductGetApiResponse;
import gift.domain.controller.apiResponse.ProductListApiResponse;
import gift.domain.controller.apiResponse.ProductOptionsGetApiResponse;
import gift.domain.dto.request.OptionAddRequest;
import gift.domain.dto.request.OptionUpdateRequest;
import gift.domain.dto.request.ProductAddRequest;
import gift.domain.dto.request.ProductUpdateRequest;
import gift.domain.dto.response.CategoryResponse;
import gift.domain.dto.response.ProductCoreInfoResponse;
import gift.domain.dto.response.ProductWithCategoryIdResponse;
import gift.domain.service.CategoryService;
import gift.domain.service.ProductService;
import gift.global.apiResponse.SuccessApiResponse;
import jakarta.validation.Valid;
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
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ProductListApiResponse> getProducts(@RequestParam("sort") String sortParams, @RequestParam("category_id") Long categoryId) {
        return SuccessApiResponse.ok(new ProductListApiResponse(
            HttpStatus.OK,
            CategoryResponse.of(categoryService.findById(categoryId)),
            productService.getAllProducts(sortParams, categoryId)
                .stream()
                .map(ProductCoreInfoResponse::of)
                .toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductGetApiResponse> getProduct(@PathVariable("id") Long id) {
        return SuccessApiResponse.ok(new ProductGetApiResponse(HttpStatus.OK, ProductWithCategoryIdResponse.of(productService.getProductById(id))));
    }

    @PostMapping
    public ResponseEntity<ProductAddApiResponse> addProduct(@Valid @RequestBody ProductAddRequest requestDto) {
        var result = productService.addProduct(requestDto);
        return SuccessApiResponse.created(
            new ProductAddApiResponse(HttpStatus.CREATED, result),
            "/api/products/{id}",
            result.id());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductUpdateRequest updateRequestDto) {
        productService.updateProductById(id, updateRequestDto);
        return SuccessApiResponse.noContent();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return SuccessApiResponse.noContent();
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<ProductOptionsGetApiResponse> getProductOptions(@PathVariable("productId") Long productId) {
        return SuccessApiResponse.ok(new ProductOptionsGetApiResponse(HttpStatus.OK, productService.getOptionsByProductId(productId)));
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<OptionAddApiResponse> addProductOption(@PathVariable("productId") Long productId, @Valid @RequestBody OptionAddRequest optionAddRequest) {
        var created = HttpStatus.CREATED;
        return SuccessApiResponse.of(new OptionAddApiResponse(created, productService.addProductOption(productId, optionAddRequest)), created);
    }

    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Void> updateProduct(@PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId,
        @Valid @RequestBody OptionUpdateRequest optionUpdateRequest
    ) {
        productService.updateProductOptionById(productId, optionId, optionUpdateRequest);
        return SuccessApiResponse.noContent();
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId, @PathVariable("optionId") Long optionId) {
        productService.deleteProductOption(productId, optionId);
        return SuccessApiResponse.noContent();
    }
}
