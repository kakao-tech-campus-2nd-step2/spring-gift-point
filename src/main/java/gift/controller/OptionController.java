package gift.controller;

import gift.dto.OptionDto;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "OptionController", description = "옵션 관련 API")
@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

  private final OptionService optionService;

  @Autowired
  public OptionController(OptionService optionService) {
    this.optionService = optionService;
  }

  @Operation(summary = "상품 옵션 조회", description = "상품 ID로 옵션을 조회합니다.")
  @GetMapping
  public ResponseEntity<List<OptionDto>> getOptionsByProductId(@PathVariable Long productId) {
    List<OptionDto> options = optionService.getOptionsByProductId(productId);
    return ResponseEntity.ok(options);
  }

  @Operation(summary = "상품 옵션 추가", description = "상품에 새로운 옵션을 추가합니다.")
  @PostMapping
  public ResponseEntity<OptionDto> addOptionToProduct(@PathVariable Long productId, @RequestBody OptionDto optionDto) {
    OptionDto createdOption = optionService.addOptionToProduct(productId, optionDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdOption);
  }
}