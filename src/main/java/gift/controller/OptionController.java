package gift.controller;

import gift.dto.common.apiResponse.ApiResponseBody.SuccessBody;
import gift.dto.common.apiResponse.ApiResponseGenerator;
import gift.dto.requestdto.OptionCreateRequestDTO;
import gift.dto.requestdto.OptionNameUpdateRequestDTO;
import gift.dto.responsedto.OptionResponseDTO;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "옵션 api", description = "옵션 api입니다")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/products/{productId}/options")
    @Operation(summary = "옵션 조회 api", description = "옵션 조회 api입니다")
    @ApiResponse(responseCode = "200", description = "옵션 조회 성공")
    public ResponseEntity<SuccessBody<List<OptionResponseDTO>>> getAllCategoriesByProductId(
        @PathVariable(value = "productId") Long productId
    ) {
        List<OptionResponseDTO> optionResponseDTOList = optionService.getAllCategoriesByProductId(
            productId);
        return ApiResponseGenerator.success(HttpStatus.OK, "모든 옵션을 조회했습니다.", optionResponseDTOList);
    }

    @PostMapping("/products/{productId}/options")
    @Operation(summary = "옵션 추가 api", description = "옵션 추가 api입니다")
    @ApiResponse(responseCode = "201", description = "옵션 추가 성공")
    public ResponseEntity<SuccessBody<OptionResponseDTO>> addOption(
        @PathVariable(value = "productId") Long productId,
        @Valid @RequestBody OptionCreateRequestDTO optionCreateRequestDTO
    ) {
        OptionResponseDTO optionResponseDTO = optionService.addOption(productId, optionCreateRequestDTO);
        return ApiResponseGenerator.success(HttpStatus.OK, "옵션이 생성되었습니다.", optionResponseDTO);
    }

    @PutMapping("/products/{productId}/options/{optionId}")
    @Operation(summary = "옵션 수정 api", description = "옵션 수정 api입니다")
    @ApiResponse(responseCode = "200", description = "옵션 수정 성공")
    public ResponseEntity<SuccessBody<OptionResponseDTO>> updateOptionName(
        @PathVariable(value = "productId") Long productId,
        @PathVariable(value = "optionId") Long optionId,
        @Valid @RequestBody OptionNameUpdateRequestDTO optionNameUpdateRequestDTO
    ) {
        OptionResponseDTO optionResponseDTO = optionService.updateOptionName(optionId, optionNameUpdateRequestDTO);
        return ApiResponseGenerator.success(HttpStatus.OK, "옵션이 수정되었습니다.", optionResponseDTO);
    }

    @DeleteMapping("/api/products/{productId}/options/{optionId}")
    @Operation(summary = "옵션 삭제 api", description = "옵션 삭제 api입니다")
    @ApiResponse(responseCode = "200", description = "옵션 삭제 성공")
    public ResponseEntity<SuccessBody<Void>> deleteOption(
        @PathVariable(value = "productId") Long productId,
        @PathVariable(value = "optionId") Long optionId
    ) {
        optionService.deleteOption(optionId);
        return ApiResponseGenerator.success(HttpStatus.OK, "옵션이 삭제되었습니다.", null);
    }
}
