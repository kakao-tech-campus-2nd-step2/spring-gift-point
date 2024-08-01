package gift.controller;

import gift.dto.OptionRequestDTO;
import gift.dto.OptionResponseDTO;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "옵션 관리 API", description = "옵션 관리를 위한 API")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "옵션 목록 조회", description = "해당 상품의 옵션들을 조회합니다.")
    public ResponseEntity<List<OptionResponseDTO>> listOptions(@PathVariable Long productId) {
        List<OptionResponseDTO> options = optionService.findALlByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @PostMapping
    @Operation(summary = "옵션 추가", description = "새로운 옵션을 추가합니다.")
    public ResponseEntity<OptionResponseDTO> addOption(@PathVariable Long productId,
        @Valid @RequestBody OptionRequestDTO optionRequestDTO) {
        OptionResponseDTO optionResponseDTO = optionService.saveOption(productId, optionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(optionResponseDTO);
    }

    @PutMapping("/{optionId}")
    @Operation(summary = "옵션 수정", description = "옵션을 수정합니다.")
    public ResponseEntity<OptionResponseDTO> updateOption(@PathVariable Long productId,
        @PathVariable Long optionId, @Valid @RequestBody OptionRequestDTO optionRequestDTO) {
        OptionResponseDTO optionResponseDTO = optionService.updateOption(productId, optionId, optionRequestDTO);
        return ResponseEntity.ok(optionResponseDTO);
    }

    @DeleteMapping("/{optionId}")
    @Operation(summary = "옵션 삭제", description = "옵션을 삭제합니다.")
    public ResponseEntity<Void> deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return ResponseEntity.noContent().build();
    }

}
