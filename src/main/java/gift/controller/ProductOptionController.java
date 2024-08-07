package gift.controller;

import gift.dto.ProductOptionDto;
import gift.service.ProductOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ProductOptionController", description = "상품 옵션 API")
@RestController
@RequestMapping("/api/products/{productId}/options")
public class ProductOptionController {

  private final ProductOptionService productOptionService;

  @Autowired
  public ProductOptionController(ProductOptionService productOptionService) {
    this.productOptionService = productOptionService;
  }

  @Operation(summary = "상품 옵션 목록 조회", description = "특정 상품의 모든 옵션을 조회합니다.")
  @GetMapping
  public List<ProductOptionDto> getOptions(@PathVariable Long productId) {
    return productOptionService.getOptionsByProductId(productId);
  }

  @Operation(summary = "상품 옵션 추가", description = "상품에 옵션을 추가합니다.")
  @PostMapping
  public ProductOptionDto addOption(@PathVariable Long productId, @RequestBody ProductOptionDto optionDto) {
    return productOptionService.addOption(productId, optionDto);
  }

  @Operation(summary = "상품 옵션 수정", description = "기존 상품 옵션의 정보를 수정합니다.")
  @PutMapping("/{optionId}")
  public ProductOptionDto updateOption(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody ProductOptionDto optionDto) {
    return productOptionService.updateOption(productId, optionId, optionDto);
  }

  @Operation(summary = "상품 옵션 삭제", description = "기존 제품 옵션을 삭제합니다.")
  @DeleteMapping("/{optionId}")
  public void deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
    productOptionService.deleteOption(productId, optionId);
  }
}