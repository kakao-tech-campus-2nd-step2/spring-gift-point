package gift.product.controller;

import gift.product.model.dto.option.CreateOptionRequest;
import gift.product.model.dto.option.OptionResponse;
import gift.product.model.dto.option.UpdateOptionRequest;
import gift.product.model.dto.product.Product;
import gift.product.service.OptionService;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
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
public class OptionController {
    private final OptionService optionService;
    private final ProductService productService;

    public OptionController(OptionService optionService, ProductService productService) {
        this.optionService = optionService;
        this.productService = productService;
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionResponse>> findOptionsByProductId(@PathVariable Long productId) {
        List<OptionResponse> options = optionService.findOptionsByProductId(productId);
        return ResponseEntity.ok().body(options);
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<String> addOption(@PathVariable Long productId,
                                            @Valid @RequestBody CreateOptionRequest createOptionRequest) {
        Product product = productService.findProduct(productId);
        optionService.addOption(product, createOptionRequest);
        return ResponseEntity.ok().body("ok");
    }

    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<String> updateOption(@PathVariable Long productId,
                                               @PathVariable Long optionId,
                                               @Valid @RequestBody UpdateOptionRequest updateOptionRequest) {
        Product product = productService.findProduct(productId);
        optionService.updateOption(product, optionId, updateOptionRequest);
        return ResponseEntity.ok().body("ok");
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<String> deleteOption(@PathVariable Long productId,
                                               @PathVariable Long optionId) {
        Product product = productService.findProduct(productId);
        optionService.deleteOption(product, optionId);
        return ResponseEntity.ok().body("ok");
    }
}
