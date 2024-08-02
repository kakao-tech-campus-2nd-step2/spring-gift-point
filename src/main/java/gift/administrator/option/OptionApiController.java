package gift.administrator.option;

import gift.response.ApiResponse;
import gift.response.ApiResponse.HttpResult;
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
@RequestMapping("/api")
@Tag(name = "Option API", description = "option related API")
public class OptionApiController {

    private final OptionService optionService;

    public OptionApiController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/products/{productId}/options")
    @Operation(summary = "get all options by productId", description = "상품 아이디로 모든 옵션을 조회합니다.")
    public ResponseEntity<ApiResponse<List<OptionDTO>>> getAllOptionsByProductId(
        @PathVariable("productId") Long productId) {
        List<OptionDTO> options = optionService.getAllOptionsByProductId(productId);
        ApiResponse<List<OptionDTO>> apiResponse = new ApiResponse<>(HttpResult.OK, "옵션 전체 조회 성공",
            HttpStatus.OK, options);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/products/{productId}/options")
    @Operation(summary = "add one option", description = "상품 아이디에 옵션을 하나 추가합니다.")
    public ResponseEntity<ApiResponse<OptionDTO>> addOptionByProductId(
        @PathVariable("productId") Long productId,
        @Valid @RequestBody OptionDTO optionDTO) {
        OptionDTO result = optionService.addOptionByProductId(productId, optionDTO);
        ApiResponse<OptionDTO> apiResponse = new ApiResponse<>(HttpResult.OK, "옵션 추가 성공",
            HttpStatus.CREATED, result);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @DeleteMapping("/products/{productId}/options/{optionId}")
    @Operation(summary = "delete one option", description = "상품 아이디와 옵션 아이디로 상품의 옵션 하나를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteOptionByOptionId(
        @PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId) {
        if (optionService.countAllOptionsByProductId(productId) == 1) {
            throw new IllegalArgumentException("상품에는 적어도 하나의 옵션이 있어야 합니다.");
        }
        optionService.deleteOptionByOptionId(optionId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(HttpResult.OK, "옵션 삭제 성공", HttpStatus.OK,
            null);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/products/{productId}/options/{optionId}")
    @Operation(summary = "update one option", description = "상품 아이디와 옵션 아이디로 옵션을 수정합니다. "
        + "required info(name and quantity)")
    public ResponseEntity<ApiResponse<OptionDTO>> updateOptionByProductIdAndOptionId(
        @PathVariable("productId") Long productId, @PathVariable("optionId") Long optionId,
        @Valid @RequestBody OptionDTO optionDTO) {
        optionService.existsByNameSameProductIdNotOptionId(optionDTO.getName(), productId,
            optionId);
        OptionDTO result = optionService.updateOption(optionId, optionDTO);
        ApiResponse<OptionDTO> apiResponse = new ApiResponse<>(HttpResult.OK, "옵션 수정 성공",
            HttpStatus.OK, result);
        return ResponseEntity.ok(apiResponse);
    }
}
