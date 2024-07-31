package gift.domain.controller;

import gift.domain.controller.apiResponse.ProductAddApiResponse;
import gift.domain.controller.apiResponse.ProductListApiResponse;
import gift.domain.controller.apiResponse.ProductOptionsGetApiResponse;
import gift.domain.dto.request.OptionAddRequest;
import gift.domain.dto.request.OptionUpdateRequest;
import gift.domain.dto.request.ProductAddRequest;
import gift.domain.dto.request.ProductUpdateRequest;
import gift.domain.dto.response.ProductWithCategoryIdResponse;
import gift.domain.service.ProductService;
import gift.global.apiResponse.BasicApiResponse;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductListApiResponse> getProducts() {
        return SuccessApiResponse.ok(new ProductListApiResponse(
            HttpStatus.OK,
            productService.getAllProductsCategories(),
            productService.getAllProducts().stream()
                .map(ProductWithCategoryIdResponse::of)
                .toList()));
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
    public ResponseEntity<BasicApiResponse> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductUpdateRequest updateRequestDto) {
        productService.updateProductById(id, updateRequestDto);
        return SuccessApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicApiResponse> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return SuccessApiResponse.ok();
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<ProductOptionsGetApiResponse> getProductOptions(@PathVariable("productId") Long productId) {
        return SuccessApiResponse.ok(new ProductOptionsGetApiResponse(HttpStatus.OK, productService.getOptionsByProductId(productId)));
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<BasicApiResponse> addProductOption(@PathVariable("productId") Long productId, @Valid @RequestBody OptionAddRequest optionAddRequest) {
        productService.addProductOption(productId, optionAddRequest);
        return SuccessApiResponse.ok();
    }

    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<BasicApiResponse> updateProduct(@PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId,
        @Valid @RequestBody OptionUpdateRequest optionUpdateRequest
    ) {
        productService.updateProductOptionById(productId, optionId, optionUpdateRequest);
        return SuccessApiResponse.ok();
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId, @PathVariable("optionId") Long optionId) {
        productService.deleteProductOption(productId, optionId);
        return SuccessApiResponse.noContent();
    }
}
