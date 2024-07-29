package gift.product.controller;

import gift.product.dto.OptionDto;
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
@RequestMapping("/products/{productId}/options")
@Tag(name = "Option API", description = "상품 옵션 관련 API")
public class OptionController {

  private final OptionService optionService;

  @Autowired
  public OptionController(OptionService optionService) {
    this.optionService = optionService;
  }

  @PostMapping
  @Operation(summary = "옵션 생성", description = "상품에 대한 새로운 옵션 생성")
  public ResponseEntity<OptionDto> createOption(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long productId,
      @Valid @RequestBody @Parameter(description = "옵션 데이터", required = true) OptionDto optionDto) {
    OptionDto createdOption = optionService.createOption(optionDto, productId);
    return new ResponseEntity<>(createdOption, HttpStatus.CREATED);
  }

  @GetMapping
  @Operation(summary = "모든 옵션 조회", description = "상품 ID로 모든 옵션을 조회")
  public ResponseEntity<List<OptionDto>> getAllOptions(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long productId) {
    List<OptionDto> options = optionService.getAllOptionsByProductId(productId);
    return ResponseEntity.ok(options);
  }

  @GetMapping("/{optionId}")
  @Operation(summary = "옵션 조회", description = "상품 ID와 옵션 ID로 특정 옵션을 조회")
  public ResponseEntity<OptionDto> getOption(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long productId,
      @PathVariable @Parameter(description = "옵션 ID", required = true) Long optionId) {
    OptionDto option = optionService.getOptionById(optionId);
    return ResponseEntity.ok(option);
  }

  @PutMapping("/{optionId}")
  @Operation(summary = "옵션 수정", description = "상품 ID와 옵션 ID로 특정 옵션을 수정")
  public ResponseEntity<OptionDto> updateOption(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long productId,
      @PathVariable @Parameter(description = "옵션 ID", required = true) Long optionId,
      @Valid @RequestBody @Parameter(description = "옵션 데이터", required = true) OptionDto optionDto) {
    optionDto.setId(optionId);
    OptionDto updatedOption = optionService.updateOption(optionDto);
    return ResponseEntity.ok(updatedOption);
  }

  @DeleteMapping("/{optionId}")
  @Operation(summary = "옵션 삭제", description = "상품 ID와 옵션 ID로 특정 옵션을 삭제")
  public ResponseEntity<Void> deleteOption(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long productId,
      @PathVariable @Parameter(description = "옵션 ID", required = true) Long optionId) {
    optionService.deleteOption(optionId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/check")
  @Operation(summary = "옵션 존재 여부 확인", description = "상품 ID와 옵션 이름으로 옵션의 존재 여부를 확인")
  public ResponseEntity<Boolean> checkOptionExists(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long productId,
      @RequestParam @Parameter(description = "옵션 이름", required = true) String name) {
    boolean exists = optionService.optionExistsByProductIdAndName(productId, name);
    return ResponseEntity.ok(exists);
  }
}