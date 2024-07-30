package gift.controller;

import gift.dto.OptionDto;
import gift.model.Option;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Option", description = "상품 옵션 관련 api")
@RequestMapping("/api/products")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "상품의 모든 옵션 조회", description = "상품의 모든 옵션을 조회합니다.")
    public ResponseEntity<List<Option>> getOptionsByProductId(@PathVariable Long productId) {
        List<Option> options = optionService.getOptionsByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 추가", description = "상품의 옵션을 추가합니다.")
    public ResponseEntity<Option> addOption(@PathVariable Long productId, @RequestBody @Valid OptionDto optionDto) {
        Option newOption = optionService.addOption(productId, optionDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{optionId}")
                .buildAndExpand(newOption.getId())
                .toUri();
        return ResponseEntity.created(location).body(newOption);
    }

    @PutMapping("/{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 수정", description = "상품의 옵션을 수정합니다.")
    public ResponseEntity<Option> updateOption(@PathVariable Long optionId, @RequestBody @Valid OptionDto optionDto) {
        Option updatedOption = optionService.updateOption(optionId, optionDto);
        return ResponseEntity.ok(updatedOption);
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 삭제", description = "상품의 옵션을 삭제합니다.")
    public ResponseEntity<Void> deleteOption(@PathVariable Long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
