package gift.controller.api;

import gift.dto.request.AddProductRequest;
import gift.dto.request.OptionRequest;
import gift.dto.request.UpdateProductRequest;
import gift.dto.response.AddedOptionIdResponse;
import gift.dto.response.AddedProductIdResponse;
import gift.dto.response.OptionResponse;
import gift.dto.response.ProductResponse;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<AddedProductIdResponse> addProduct(@Valid @RequestBody AddProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(request));
    }

    @GetMapping("/api/products")
    public ResponseEntity<Page<ProductResponse>> getProducts(@PageableDefault(sort = "id") Pageable pageable,
                                                             @RequestParam(value = "categoryId", required = false) Long categoryId) {
        Page<ProductResponse> productResponses = productService.getProductResponsesByCategoryId(pageable, categoryId);
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/api/products/{id}")
    public ResponseEntity<ProductResponse> getProductResponse(@PathVariable("id") Long productId) {
        ProductResponse productResponse = productService.getProductResponse(productId);
        return ResponseEntity.ok(productResponse);
    }

    @PutMapping("/api/products/{id}")
    public ResponseEntity<Void> updateProduct(@Valid @RequestBody UpdateProductRequest request, @PathVariable("id") Long productId) {
        productService.updateProduct(request, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/products/{id}/options")
    public ResponseEntity<AddedOptionIdResponse> addOptionToProduct(@PathVariable("id") Long productId,
                                                                    @Valid @RequestBody OptionRequest optionRequest) {
        AddedOptionIdResponse addedOptionId = productService.addOptionToProduct(productId, optionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedOptionId);
    }

    @GetMapping("/api/products/{id}/options")
    public ResponseEntity<List<OptionResponse>> getOptionResponses(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getOptionResponses(productId));
    }

    @PutMapping("/api/products/{productId}/options")
    public ResponseEntity<List<OptionResponse>> updateOption(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getOptionResponses(productId));
    }


}
