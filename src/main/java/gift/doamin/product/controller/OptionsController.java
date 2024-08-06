package gift.doamin.product.controller;

import gift.doamin.product.dto.OptionRequest;
import gift.doamin.product.dto.OptionResponse;
import gift.doamin.product.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품 옵션", description = "상품 옵션 관련 API")
@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionsController {

    private final OptionService optionService;

    public OptionsController(OptionService optionService) {
        this.optionService = optionService;
    }

    @Operation(summary = "옵션 추가", description = "선택한 상품에 새로운 옵션을 추가합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OptionResponse addOption(@PathVariable Long productId,
        @Valid @RequestBody OptionRequest optionRequest) {
        return optionService.create(productId, optionRequest);
    }

    @Operation(summary = "옵션 수정", description = "선택한 옵션의 이름이나 수량을 수정합니다.")
    @PutMapping("/{optionId}")
    public OptionResponse updateOption(@PathVariable Long productId, @PathVariable Long optionId,
        @Valid @RequestBody OptionRequest optionRequest) {
        return optionService.update(productId, optionId, optionRequest);
    }

    @Operation(summary = "옵션 삭제", description = "선택한 옵션을 삭제합니다.")
    @DeleteMapping("/{optionId}")
    public void deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.delete(productId, optionId);
    }

    @Operation(summary = "옵션 조회", description = "선택한 상품의 옵션 목록을 조회합니다.")
    @GetMapping
    public List<OptionResponse> getAllOptions(@PathVariable Long productId) {
        return optionService.findAll(productId);
    }
}
