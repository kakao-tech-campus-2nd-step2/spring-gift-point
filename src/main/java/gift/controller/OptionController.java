package gift.controller;

import gift.domain.Option;
import gift.dto.OptionResponse;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Option API", description = "상품 옵션 관련 엔드포인트")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "상품 옵션 목록 조회", description = "특정 상품의 옵션을 조회합니다.")
    public ResponseEntity<List<OptionResponse>> getOptions(@PathVariable("productId") Long productId) {
        List<Option> options = optionService.getOptionsByProductId(productId);
        if (options == null) {
            return ResponseEntity.notFound().build();
        }
        List<OptionResponse> optionDTOs = options.stream().map(OptionResponse::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(optionDTOs);
    }

    @PostMapping
    @Operation(summary = "상품 옵션 추가", description = "특정 상품에 옵션을 추가합니다.")
    public ResponseEntity<String> addOptionToProduct(@PathVariable("productId") Long productId, @RequestBody @Valid Option option) {
        try {
            optionService.addOptionToProduct(productId, option);
            return ResponseEntity.status(HttpStatus.CREATED).body("옵션 추가 완료!");
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("동일한 상품 내에 동일한 옵션 이름이 존재합니다.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/{optionId}")
    @Operation(summary = "상품 옵션 수정", description = "특정 상품의 기존 옵션 정보를 수정합니다.")
    public ResponseEntity<String> editOption(@PathVariable("productId") Long productId, @PathVariable("optionId") Long optionId, @RequestBody @Valid Option option) {
        try {
            optionService.updateOption(productId, optionId, option);
            return ResponseEntity.ok("옵션 수정 완료!");
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("동일한 상품 내에 동일한 옵션 이름이 존재합니다.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{optionId}")
    @Operation(summary = "상품 옵션 삭제", description = "특정 상품의 옵션을 삭제합니다.")
    public ResponseEntity<String> deleteOption(@PathVariable("productId") Long productId, @PathVariable("optionId") Long optionId) {
        try {
            optionService.deleteOption(productId, optionId);
            return ResponseEntity.ok("옵션 삭제 완료!");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
