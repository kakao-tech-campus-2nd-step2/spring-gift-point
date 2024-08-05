package gift.product.presentation;

import gift.option.application.OptionService;
import gift.option.application.OptionServiceResponse;
import gift.option.presentation.request.OptionCreateRequest;
import gift.option.presentation.request.OptionUpdateRequest;
import gift.product.application.ProductService;
import gift.product.application.ProductServiceResponse;
import gift.product.presentation.request.ProductCreateRequest;
import gift.product.presentation.response.ProductReadResponse;
import gift.product.presentation.request.ProductUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.data.domain.Sort.*;
import static org.springframework.data.domain.Sort.Direction.*;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/products")
public class ProductController implements ProductApi {
    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProductCreateRequest request) {
        Long productId = productService.save(request.toCommand());

        return ResponseEntity.created(URI.create("/api/products/" + productId)).build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductReadResponse>> findAll(
            @PageableDefault(sort = "name", direction = ASC) Pageable pageable,
            @RequestParam(required = false) Long categoryId
    ) {
        Page<ProductServiceResponse> products;
        if (categoryId != null) {
            products = productService.findAllByCategoryId(pageable, categoryId);
        } else {
            products = productService.findAll(pageable);
        }
        return ResponseEntity.ok(products.map(ProductReadResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductReadResponse> findById(
            @PathVariable("id") Long productId
    ) {
        return ResponseEntity.ok(ProductReadResponse.from(productService.findById(productId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Long productId,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        productService.update(request.toCommand(productId));

        return ResponseEntity.created(URI.create("/api/products/" + productId)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") Long productId
    ) {
        productService.delete(productId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/options")
    public ResponseEntity<List<OptionServiceResponse>> findOptionsByProductId(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(optionService.findOptionsByProductId(productId));
    }

    @PostMapping("/{id}/options")
    public ResponseEntity<?> addOption(
            @PathVariable("id") Long productId,
            OptionCreateRequest request
    ) {
        optionService.save(request.toCommand(productId));

        return ResponseEntity.status(CREATED).build();
    }

    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<?> updateOption(
            @PathVariable("productId") Long productId,
            @PathVariable("optionId") Long optionId,
            OptionUpdateRequest request
    ) {
        optionService.update(request.toCommand(optionId, productId));

        return ResponseEntity.created(URI.create("/api/products/" + productId + "/options/" + optionId)).build();
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<?> deleteOption(
            @PathVariable("productId") Long productId,
            @PathVariable("optionId") Long optionId
    ) {
        optionService.delete(optionId, productId);

        return ResponseEntity.noContent().build();
    }
}
