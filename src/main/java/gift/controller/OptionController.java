package gift.controller;

import gift.dto.OptionDTO;
import gift.entity.OptionEntity;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "상품 옵션 API")
@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    // 전체 옵션 조회
    @Operation(summary = "전체 옵션 조회", description = "모든 옵션 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<OptionEntity>> getProductAllOption(@PathVariable Long productId) {
        List<OptionEntity> options = optionService.readProductAllOption(productId);
        return ResponseEntity.ok(options);
    }

    // 단일 옵션 조회
    @Operation(summary = "단일 옵션 조회", description = "단일 옵션 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<OptionEntity> getProductOption(@PathVariable Long productId, @PathVariable Long id) {
        OptionEntity option = optionService.readProductOption(productId, id);
        return ResponseEntity.ok(option);
    }

    // 옵션 생성
    @Operation(summary = "옵션 생성", description = "새 옵션을 생성합니다.")
    @PostMapping
    public ResponseEntity<?> createOption(@PathVariable Long productId, @Valid @RequestBody OptionDTO optionDTO) {
        optionService.createOption(productId, optionDTO);
        return ResponseEntity.ok("옵션이 생성 되었습니다.");
    }

    // 옵션 수정
    @Operation(summary = "옵션 수정", description = "지정된 옵션 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<?> editOption(@PathVariable Long productId, @PathVariable Long id, @Valid @RequestBody OptionDTO optionDTO) {
        optionService.editOption(productId, id, optionDTO);
        return ResponseEntity.ok("옵션이 수정 되었습니다.");
    }

    // 옵션 삭제
    @Operation(summary = "옵션 삭제", description = "지정된 옵션을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOption(@PathVariable Long productId, @PathVariable Long id) {
        optionService.deleteOption(productId, id);
        return ResponseEntity.ok("옵션이 삭제 되었습니다.");
    }
}