package gift.controller;

import gift.domain.model.dto.OptionAddRequestDto;
import gift.domain.model.dto.OptionResponseDto;
import gift.domain.model.dto.OptionUpdateRequestDto;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Validated
@Tag(name = "Option", description = "상품 옵션 관리 API")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }


    @Operation(summary = "상품의 모든 옵션 조회", description = "지정된 상품 ID의 모든 옵션을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<OptionResponseDto>> getAllOptionsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(optionService.getAllOptionsByProductId(productId));
    }

    @Operation(summary = "옵션 추가", description = "지정된 상품에 새로운 옵션을 추가합니다.")
    @PostMapping
    public ResponseEntity<OptionResponseDto> addOption(
        @PathVariable Long productId,
        @Valid @RequestBody OptionAddRequestDto optionAddRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(optionService.addOption(productId,
            optionAddRequestDto));
    }

    @Operation(summary = "옵션 수정", description = "지정된 ID의 옵션을 수정합니다.")
    @PutMapping("/{optionId}")
    public ResponseEntity<OptionResponseDto> updateOption(
        @PathVariable Long optionId,
        @Valid @RequestBody OptionUpdateRequestDto optionUpdateRequestDto) {
        return ResponseEntity.ok(optionService.updateOption(optionId, optionUpdateRequestDto));
    }

    @Operation(summary = "옵션 삭제", description = "지정된 ID의 옵션을 삭제합니다.")
    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
}