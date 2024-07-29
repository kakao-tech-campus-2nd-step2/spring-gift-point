package gift.administrator.option;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Option API", description = "option related API")
public class OptionApiController {

    private final OptionService optionService;

    public OptionApiController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/products/{productId}/options")
    @Operation(summary = "get all options by productId", description = "상품 아이디로 모든 옵션을 조회합니다.")
    public ResponseEntity<List<OptionDTO>> getAllOptionsByProductId(
        @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(optionService.getAllOptionsByProductId(productId));
    }

    @GetMapping("/options")
    @Operation(summary = "get all options", description = "모든 옵션을 조회합니다.")
    public ResponseEntity<List<OptionDTO>> getAllOptions() {
        return ResponseEntity.ok(optionService.getAllOptions());
    }

    @DeleteMapping("/options/{optionId}")
    @Operation(summary = "delete one option", description = "옵션 아이디로 옵션 하나를 삭제합니다.")
    public ResponseEntity<Void> deleteOptionByOptionId(
        @PathVariable("optionId") long optionId) {
        if (optionService.countAllOptionsByProductIdFromOptionId(optionId) == 1) {
            throw new IllegalArgumentException("상품에는 적어도 하나의 옵션이 있어야 합니다.");
        }
        optionService.deleteOptionByOptionId(optionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/products/{productId}/options/{optionId}")
    @Operation(summary = "update one option", description = "상품 아이디와 옵션 아이디로 옵션을 수정합니다. "
        + "required info(name and quantity)")
    public ResponseEntity<OptionDTO> updateOptionByProductIdAndOptionId(
        @PathVariable("productId") long productId, @PathVariable("optionId") long optionId,
        @Valid @RequestBody OptionDTO optionDTO) {
        optionService.existsByNameSameProductIdNotOptionId(optionDTO.getName(), productId,
            optionId);
        return ResponseEntity.ok(optionService.updateOption(optionId, optionDTO));
    }
}
