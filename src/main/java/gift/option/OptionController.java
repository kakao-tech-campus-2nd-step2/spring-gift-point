package gift.option;

import gift.option.dto.OptionRequestDTO;
import gift.option.dto.OptionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Option", description = "Option API")
@RestController
@RequestMapping("/api/products")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/{productId}/option")
    @Operation(summary = "옵션 조회", description = "상품의 모든 옵션을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "정상")
    @ApiResponse(responseCode = "400", description = "상품이 존재하지 않는 경우")
    @ApiResponse(responseCode = "400", description = "요청 양식이 잘못된 경우")
    @ApiResponse(responseCode = "500", description = "서버 에러")
    public List<OptionResponseDTO> getOption(@PathVariable("productId") long productId) {
        return optionService.getOptions(productId);
    }

    @Deprecated
    @PostMapping("/{productId}/option")
    public void addOption(
        @PathVariable("productId") long productId,
        @RequestBody OptionRequestDTO optionDTO
    ) {
        optionService.addOption(productId, optionDTO);
    }

    @Deprecated
    @PatchMapping("/{productId}/option")
    public void updateOption(
        @PathVariable("productId") long productId,
        @RequestBody OptionRequestDTO optionDTO
    ) {
        optionService.updateOption(productId, optionDTO);
    }

    @Deprecated
    @DeleteMapping("/{productId}/option/{optionId}")
    public void deleteOption(
        @PathVariable("productId") long productId,
        @PathVariable("optionId") long optionId
    ) {
        optionService.deleteOption(productId, optionId);
    }
}
