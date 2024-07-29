package gift.controller.api;

import gift.dto.OptionDTO;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Option API", description = "옵션 관련 API")
public class OptionController {

    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "상품 ID로 옵션 조회", description = "지정된 상품 ID에 해당하는 모든 옵션을 조회합니다.")
    public List<OptionDTO> getOptionsByProductId(@PathVariable Long productId) {
        return optionService.getOptionsByProductId(productId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "옵션 ID로 옵션 조회", description = "지정된 옵션 ID에 해당하는 옵션을 조회합니다.")
    public OptionDTO getOptionById(@PathVariable Long id) {
        return optionService.getOptionById(id);
    }

    @PostMapping
    @Operation(summary = "옵션 추가", description = "새로운 옵션을 추가합니다.")
    public void addOption(@RequestBody OptionDTO optionDTO) {
        optionService.saveOption(optionDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "옵션 수정", description = "지정된 옵션 ID에 해당하는 옵션을 수정합니다.")
    public void updateOption(@PathVariable Long id, @RequestBody OptionDTO optionDTO) {
        optionService.updateOption(id, optionDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "옵션 삭제", description = "지정된 옵션 ID에 해당하는 옵션을 삭제합니다.")
    public void deleteOption(@PathVariable Long id) {
        optionService.deleteOption(id);
    }
}
