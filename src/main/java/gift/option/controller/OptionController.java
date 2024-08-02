package gift.option.controller;

import gift.common.util.CommonResponse;
import gift.option.dto.OptionRequest;
import gift.option.dto.OptionResponse;
import gift.option.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/products")
@Tag(name = "상품 옵션 API", description = "상품 옵션 관리 API")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    // 옵션 등록 처리
    @Operation(summary = "상품 옵션 추가", description = "상품에 옵션을 추가한다.")
    @PostMapping("/{productId}/options")
    public ResponseEntity<?> createOption(@PathVariable("productId") Long productId, @Valid @RequestBody OptionRequest optionRequest) {
        optionService.createOption(productId, optionRequest);

        //return "redirect:/show/products/" + optionRequest.getProductId() + "/options";
        return ResponseEntity.ok(new CommonResponse<>(null, "상품 옵션 추가 완료", true));
    }

    // 옵션 수정 처리
    @Operation(summary = "상품 옵션 수정", description = "기존 상품 옵션의 정보를 수정한다.")
    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<?> updateOption(@PathVariable("productId") Long ProductId, @PathVariable("optionId") Long optionId, @Valid @RequestBody OptionRequest optionRequest){
        optionService.updateOption(ProductId, optionRequest, optionId);

        //return "redirect:/show/products/" + optionRequest.getProductId() + "/options";
        return ResponseEntity.ok(new CommonResponse<>(null, "상품 옵션 수정 완료", true));
    }

    // 옵션 삭제
    @Operation(summary = "상품 옵션 삭제", description = "기존 제품 옵션을 삭제한다.")
    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<?> deleteOption(@PathVariable("optionId") Long optionId, @PathVariable("productId") Long productId){
        optionService.deleteOption(optionId, productId);

        //return "redirect:/show/products/" + productId + "/options";
        return ResponseEntity.ok(new CommonResponse<>(null, "상품 옵션 삭제 완료", true));
    }

    // 상품 옵션 목록 조회
    @Operation(summary = "상품 옵션 목록 조회", description = "특정 상품에 대한 모든 옵션을 조회한다.")
    @GetMapping("/{productId}/options")
    public ResponseEntity<?> getOptions(@PathVariable Long productId) {
        List<OptionResponse> options = optionService.getOptions(productId);

        return ResponseEntity.ok(new CommonResponse<>(options, "상품 옵션 목록 조회 완료", true));
    }
}
