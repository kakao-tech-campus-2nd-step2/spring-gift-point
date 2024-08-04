package gift.product.presentation;

import gift.option.application.OptionServiceResponse;
import gift.product.application.ProductService;
import gift.product.presentation.request.ProductCreateRequest;
import gift.product.presentation.request.ProductUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController implements ProductApi {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    public void create(@Valid @RequestBody ProductCreateRequest request) {
        productService.save(request.toCommand());
    }

    @GetMapping("")
    public ResponseEntity<Page<ProductControllerResponse>> findAll(
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.findAll(pageable).map(ProductControllerResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductControllerResponse> findById(
            @PathVariable("id") Long productId
    ) {
        return ResponseEntity.ok(ProductControllerResponse.from(productService.findById(productId)));
    }

    @PutMapping("/{id}")
    public void update(
            @PathVariable("id") Long productId,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        productService.update(request.toCommand(productId));
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id") Long productId
    ) {
        productService.delete(productId);
    }

    @GetMapping("/{id}/options")
    public ResponseEntity<List<OptionServiceResponse>> findOptionsByProductId(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.findOptionsByProductId(productId));
    }
}
