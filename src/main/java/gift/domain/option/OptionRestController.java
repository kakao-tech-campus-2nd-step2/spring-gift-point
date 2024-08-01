package gift.domain.option;

import gift.domain.option.dto.request.OptionRequest;
import gift.domain.option.dto.response.OptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/api/products")
@Tag(name = "Option", description = "Option API")
public class OptionRestController {

    private final OptionService optionService;

    public OptionRestController(OptionService optionService) {
        this.optionService = optionService;
    }

    /**
     * 특정 상품의 옵션 목록 조회
     */
    @GetMapping("/{productId}/options")
    @Operation(summary = "특정 상품의 옵션 목록 조회")
    public ResponseEntity<List<OptionResponse>> getOptions(
        @Parameter(description = "상품 ID") @PathVariable("productId") Long productId
    ) {
        List<OptionResponse> optionsResponse = optionService.getOptions(productId);
        return ResponseEntity.ok(optionsResponse);
    }

    /**
     * 특정 상품에 옵션 추가
     */
    @PostMapping("/{productId}/options")
    @Operation(summary = "특정 상품에 옵션 추가")
    public ResponseEntity addOption(
        @Parameter(description = "상품 ID") @PathVariable("productId") Long productId,
        @Valid @RequestBody gift.domain.option.dto.request.OptionRequest optionRequest
    ) {
        optionService.addOptionToExistsProduct(productId, optionRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 상품의 옵션 수정
     */
    @PutMapping("/{productId}/options/{optionId}")
    @Operation(summary = "특정 상품의 옵션 수정")
    public ResponseEntity updateOption(
        @Parameter(description = "상품 ID") @PathVariable("productId") Long productId,
        @Parameter(description = "옵션 ID") @PathVariable("optionId") Long optionId,
        @Valid @RequestBody OptionRequest optionRequest
    ) {
        optionService.updateOption(productId, optionId, optionRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 상품의 옵션 삭제
     */
    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity deleteOption(
        @Parameter(description = "상품 ID") @PathVariable("productId") Long productId,
        @Parameter(description = "옵션 ID") @PathVariable("optionId") Long optionId
    ) {
        optionService.deleteOption(productId, optionId);
        return ResponseEntity.ok().build();
    }


}
