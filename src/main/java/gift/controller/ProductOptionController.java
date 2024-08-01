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
@RequestMapping("/api/products")
public class ProductOptionController {

  private final ProductOptionService productOptionService;

  @Autowired
  public ProductOptionController(ProductOptionService productOptionService) {
    this.productOptionService = productOptionService;
  }

  @Operation(summary = "상품 옵션 조회", description = "특정 상품의 옵션을 조회합니다.")
  @GetMapping("/{productId}/product-options")
  public List<ProductOptionDto> getOptions(@PathVariable Long productId) {
    return productOptionService.getOptionsByProductId(productId);
  }
}