package gift.product.controller;

import gift.product.dto.OptionRequestDto;
import gift.product.dto.OptionResponseDto;
import gift.product.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Option API", description = "상품 옵션 관련 API")
public class OptionController {

  private final OptionService optionService;

  @Autowired
  public OptionController(OptionService optionService) {
    this.optionService = optionService;
  }

  @PostMapping
  @Operation(summary = "상품 옵션 추가", description = "상품에 옵션을 추가한다.")
  public ResponseEntity<OptionResponseDto> createOption(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long productId,
      @Valid @RequestBody @Parameter(description = "옵션 데이터", required = true) OptionRequestDto optionDto) {
    OptionResponseDto optionResponseDto = optionService.createOption(optionDto, productId);
    return new ResponseEntity<>(optionResponseDto, HttpStatus.CREATED);
  }

  @GetMapping
  @Operation(summary = "상품 옵션 목록 조회", description = "특정 상품에 대한 모든 옵션을 조회한다.")
  public ResponseEntity<List<OptionResponseDto>> getAllOptions(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long productId) {
    List<OptionResponseDto> options = optionService.getProductOptions(productId);
    return ResponseEntity.ok(options);
  }

  @PutMapping("/{optionId}")
  @Operation(summary = "옵션 수정", description = "기존 상품 옵션의 정보를 수정한다.")
  public ResponseEntity<OptionResponseDto> updateOption(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long productId,
      @PathVariable @Parameter(description = "옵션 ID", required = true) Long optionId,
      @Valid @RequestBody @Parameter(description = "옵션 데이터", required = true) OptionRequestDto optionRequestDto) {

    OptionResponseDto optionResponseDto = optionService.updateOption(optionRequestDto, productId, optionId);
    return ResponseEntity.ok(optionResponseDto);
  }

  @DeleteMapping("/{optionId}")
  @Operation(summary = "옵션 삭제", description = "기존 제품 옵션을 삭제한다.")
  public ResponseEntity<OptionResponseDto> deleteOption(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long productId,
      @PathVariable @Parameter(description = "옵션 ID", required = true) Long optionId) {
    OptionResponseDto deletedOption = optionService.deleteOption(productId, optionId);
    return new ResponseEntity<>(deletedOption, HttpStatus.NO_CONTENT);
  }
}
