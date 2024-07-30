package gift.controller;

import gift.dto.OptionDTO;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Option", description = "옵션 관련 API")
@RestController
@RequestMapping("/api/products/{product_id}/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @Operation(summary = "옵션 조회", description = "해당 상품의 모든 옵션을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<OptionDTO>> getOptions(@PathVariable("product_id") long productId) {
        return ResponseEntity.ok().body(optionService.getOptions(productId));
    }

    @Operation(summary = "옵션 추가", description = "해당 상품에 옵션을 추가합니다.")
    @PostMapping
    public ResponseEntity<OptionDTO> addOption(@PathVariable("product_id") long productId, @Valid @RequestBody OptionDTO optionDTO) {
        return ResponseEntity.ok().body(optionService.addOption(productId, optionDTO));
    }

    @Operation(summary = "옵션 수정", description = "해당 상품의 특정 옵션을 수정합니다.")
    @PutMapping
    public ResponseEntity<OptionDTO> updateOption(@PathVariable("product_id") long productId, @Valid @RequestBody OptionDTO optionDTO) {
        return ResponseEntity.ok().body(optionService.updateOption(productId, optionDTO));
    }

    @Operation(summary = "옵션 삭제", description = "해당 id의 옵션을 삭제합니다.")
    @DeleteMapping("{id}")
    public ResponseEntity<OptionDTO> deleteOption(@PathVariable("product_id") long productId, @PathVariable("id") long id) {
        return ResponseEntity.ok().body(optionService.deleteOption(productId, id));
    }
}
