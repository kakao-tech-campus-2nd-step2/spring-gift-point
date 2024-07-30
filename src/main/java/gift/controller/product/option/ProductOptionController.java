package gift.controller.product.option;

import gift.domain.product.option.ProductOption;
import gift.service.product.option.ProductOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/options")
public class ProductOptionController {

    @Autowired
    private ProductOptionService productOptionService;

    private static final Logger logger = LoggerFactory.getLogger(ProductOptionController.class);

    @Operation(summary = "상품 옵션 추가", description = "주어진 상품 ID로 새로운 옵션을 추가합니다.")
    @PostMapping("/{productId}")
    public ResponseEntity<ProductOption> addProductOption(
            @Parameter(description = "옵션을 추가할 상품의 ID", required = true)
            @PathVariable Long productId,
            @Parameter(description = "추가할 옵션 정보", required = true)
            @RequestBody ProductOptionRequest request) {
        logger.info("Received addProductOption request for productId {}", productId);
        ProductOption option = productOptionService.addProductOption(productId, request.getName(), request.getQuantity());
        logger.info("Added product option: {}", option);
        return ResponseEntity.ok(option);
    }

    @Operation(summary = "상품 옵션 조회", description = "주어진 상품 ID로 모든 옵션을 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductOption>> getProductOptions(
            @Parameter(description = "옵션을 조회할 상품의 ID", required = true)
            @PathVariable Long productId) {
        logger.info("Received getProductOptions request for productId {}", productId);
        List<ProductOption> options = productOptionService.getProductOptions(productId);
        logger.info("Retrieved {} product options", options.size());
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "상품 옵션 수량 차감", description = "주어진 상품 ID와 옵션 이름으로 옵션의 수량을 차감합니다.")
    @PostMapping("/{productId}/subtract")
    public ResponseEntity<Void> subtractProductOptionQuantity(
            @Parameter(description = "수량을 차감할 상품의 ID", required = true)
            @PathVariable Long productId,
            @Parameter(description = "차감할 수량 정보", required = true)
            @RequestBody SubtractProductOptionQuantityRequest request) {
        logger.info("Received subtractProductOptionQuantity request for productId {} and option {}", productId, request.getOptionName());
        productOptionService.subtractProductOptionQuantity(productId, request.getOptionName(), request.getQuantity());
        logger.info("Subtracted {} from option {} for productId {}", request.getQuantity(), request.getOptionName(), productId);
        return ResponseEntity.ok().build();
    }

    static class ProductOptionRequest {
        private String name;
        private Long quantity;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Long getQuantity() { return quantity; }
        public void setQuantity(Long quantity) { this.quantity = quantity; }
    }

    static class SubtractProductOptionQuantityRequest {
        private String optionName;
        private Long quantity;

        public String getOptionName() { return optionName; }
        public void setOptionName(String optionName) { this.optionName = optionName; }
        public Long getQuantity() { return quantity; }
        public void setQuantity(Long quantity) { this.quantity = quantity; }
    }
}
