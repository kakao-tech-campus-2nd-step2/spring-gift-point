package gift.controller;

import gift.domain.Product;
import gift.dto.option.CreateOptionRequest;
import gift.dto.option.OptionResponse;
import gift.dto.option.UpdateOptionRequest;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Option", description = "Option API")
public class OptionController {
    private final OptionService optionService;
    private final ProductService productService;

    public OptionController(OptionService optionService, ProductService productService) {
        this.optionService = optionService;
        this.productService = productService;
    }

    @Operation(summary = "옵션 id로 옵션 상세 조회")
    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionResponse>> findOptionsByProductId(@PathVariable Long productId) {
        List<OptionResponse> options = optionService.findOptionsByProductId(productId);
        return ResponseEntity.ok().body(options);
    }

    @Operation(summary = "상품 id로 옵션 추가")
    @PostMapping("/{productId}/options")
    public ResponseEntity<String> addOption(@PathVariable Long productId,
                                            @Valid @RequestBody CreateOptionRequest createOptionRequest) {
        Product product = productService.findProduct(productId);
        optionService.addOption(product, createOptionRequest);
        return ResponseEntity.ok().body("ok");
    }

    @Operation(summary = "옵션 Id로 옵션 수정")
    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<String> updateOption(@PathVariable Long productId,
                                               @PathVariable Long optionId,
                                               @Valid @RequestBody UpdateOptionRequest updateOptionRequest) {
        Product product = productService.findProduct(productId);
        optionService.updateOption(product, optionId, updateOptionRequest);
        return ResponseEntity.ok().body("ok");
    }

    @Operation(summary = "옵션 id로 옵션 삭제", description = "상품에 옵션이 하나 이상 남아있을 때 옵션 id로 옵션 삭제")
    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<String> deleteOption(@PathVariable Long productId,
                                               @PathVariable Long optionId) {
        Product product = productService.findProduct(productId);
        optionService.deleteOption(product, optionId);
        return ResponseEntity.ok().body("ok");
    }
}
