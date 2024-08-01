package gift.controller;

import gift.dto.OptionRequestDTO;
import gift.dto.OptionResponseDTO;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "OptionController", description = "옵션 관리 API")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @Operation(summary = "옵션 수량 차감", description = "수량만큼 옵션의 수량을 차감합니다.")
    @PostMapping("/{optionId}/subtract")
    public ResponseEntity<Void> subtractOptionQuantity(@PathVariable Long productId, @PathVariable Long optionId, @RequestParam int quantity) {
        optionService.subtractOptionQuantity(productId, optionId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "옵션 조회", description = "옵션을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<OptionResponseDTO>> getOptions(@PathVariable Long productId) {
        List<OptionResponseDTO> options = optionService.getOptionsByProductId(productId);
        return new ResponseEntity<>(options, HttpStatus.OK);
    }

    @Operation(summary = "옵션 추가", description = "옵션을 추가합니다.")
    @PostMapping
    public ResponseEntity<Void> addOption(@PathVariable Long productId, @RequestBody OptionRequestDTO optionRequest) {
        optionService.addOption(productId, optionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "옵션 삭제", description = "옵션을 삭제합니다.")
    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long optionId) {
        optionService.deleteOption(optionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}