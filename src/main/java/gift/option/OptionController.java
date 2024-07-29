package gift.option;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "400", description = "존재하지 않는 상품")
    public List<OptionDTO> getOption(@PathVariable("productId") long productId) {
        return optionService.getOptions(productId);
    }

    @PostMapping("/{productId}/option")
    @Operation(summary = "옵션 추가", description = "상품에 옵션을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "추가 성공")
    @ApiResponse(responseCode = "400", description = "존재하지 않는 상품이거나, 잘못된 옵션 정보 입력입니다.")
    public void addOption(
        @PathVariable("productId") long productId,
        @Valid @RequestBody OptionDTO optionDTO
    ) {
        optionService.addOption(productId, optionDTO);
    }

    @PatchMapping("/{productId}/option")
    @Operation(summary = "옵션 수정", description = "상품의 옵션을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공")
    @ApiResponse(responseCode = "400", description = "존재하지 않는 상품이거나, 잘못된 옵션 정보 입력입니다.")
    public void updateOption(
        @PathVariable("productId") long productId,
        @Valid @RequestBody OptionDTO optionDTO
    ) {
        optionService.updateOption(productId, optionDTO);
    }

    @DeleteMapping("/{productId}/option/{optionId}")
    @Operation(summary = "옵션 삭제", description = "상품의 옵션을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @ApiResponse(responseCode = "400", description = "존재하지 않는 상품이거나, 존재하지 않는 옵션입니다.")
    public void deleteOption(
        @PathVariable("productId") long productId,
        @PathVariable("optionId") long optionId
    ) {
        optionService.deleteOption(productId, optionId);
    }
}
