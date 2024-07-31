package gift.product.application;

import gift.product.application.dto.request.ProductOptionRequest;
import gift.product.application.dto.response.ProductOptionResponse;
import gift.product.service.ProductOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ProductOption", description = "상품 옵션 관련 API")
@RestController
@RequestMapping("/api/products/{productId}/options")
public class ProductOptionController {
    private final ProductOptionService productOptionService;

    public ProductOptionController(ProductOptionService productOptionService) {
        this.productOptionService = productOptionService;
    }

    @Operation(summary = "상품 옵션 등록", description = "상품 옵션을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "상품 옵션 등록 성공")
    @PostMapping
    public ResponseEntity<Void> createProductOption(@PathVariable Long productId,
                                                    @Valid @RequestBody ProductOptionRequest request
    ) {
        var optionId = productOptionService.createProductOption(productId, request.toProductOptionCommand());

        return ResponseEntity.created(URI.create("/api/products/" + productId + "/options/" + optionId))
                .build();
    }

    @Operation(summary = "상품 옵션 수정", description = "상품 옵션을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "상품 옵션 수정 성공")
    @PatchMapping("/{optionId}")
    @ResponseStatus(HttpStatus.OK)
    public void modifyProductOption(@PathVariable Long productId,
                                    @PathVariable Long optionId,
                                    @Valid @RequestBody ProductOptionRequest request
    ) {
        productOptionService.modifyProductOption(productId, optionId, request.toProductOptionCommand());
    }

    @Operation(summary = "상품 옵션 조회", description = "상품 옵션을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 옵션 조회 성공")
    @GetMapping("/{optionId}")
    public ResponseEntity<ProductOptionResponse> getProductOption(@PathVariable Long productId,
                                                                  @PathVariable Long optionId
    ) {
        var productOption = productOptionService.getProductOptionInfo(productId, optionId);

        var response = ProductOptionResponse.from(productOption);
        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "상품 옵션 목록 조회", description = "상품 옵션 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 옵션 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<ProductOptionResponse>> getAllProductOptions(@PathVariable Long productId) {
        var productOptions = productOptionService.getAllProductOptions(productId);

        var response = productOptions.stream()
                .map(ProductOptionResponse::from)
                .toList();
        return ResponseEntity.ok()
                .body(response);
    }
}
