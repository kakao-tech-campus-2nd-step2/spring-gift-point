package gift.controller;

import gift.dto.common.apiResponse.ApiResponseBody.SuccessBody;
import gift.dto.common.apiResponse.ApiResponseGenerator;
import gift.dto.requestdto.OptionCreateRequestDTO;
import gift.dto.requestdto.OptionNameUpdateRequestDTO;
import gift.dto.responsedto.OptionResponseDTO;
import gift.service.OptionService;
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
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/products/{productId}/options")
    public ResponseEntity<SuccessBody<List<OptionResponseDTO>>> getAllCategoriesByProductId(
        @PathVariable(value = "productId") Long productId
    ) {
        List<OptionResponseDTO> optionResponseDTOList = optionService.getAllCategoriesByProductId(
            productId);
        return ApiResponseGenerator.success(HttpStatus.OK, "모든 옵션을 조회했습니다.", optionResponseDTOList);
    }

    @PostMapping("/products/{productId}/options")
    public ResponseEntity<SuccessBody<Long>> addOption(
        @PathVariable(value = "productId") Long productId,
        @Valid @RequestBody OptionCreateRequestDTO optionCreateRequestDTO
    ) {
        Long optionId = optionService.addOption(productId, optionCreateRequestDTO);
        return ApiResponseGenerator.success(HttpStatus.CREATED, "옵션이 생성되었습니다.", optionId);
    }

    @PutMapping("/options/{optionId}")
    public ResponseEntity<SuccessBody<Long>> updateOptionName(
        @PathVariable(value = "optionId") Long optionId,
        @Valid @RequestBody OptionNameUpdateRequestDTO optionNameUpdateRequestDTO
    ) {
        Long updatedOptionId = optionService.updateOptionName(optionId, optionNameUpdateRequestDTO);
        return ApiResponseGenerator.success(HttpStatus.OK, "옵션이 수정되었습니다.", updatedOptionId);
    }

    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<SuccessBody<Long>> deleteOption(
        @PathVariable(value = "optionId") Long optionId
    ) {
        Long deletedOptionId = optionService.deleteOption(optionId);
        return ApiResponseGenerator.success(HttpStatus.OK, "옵션이 삭제되었습니다.", deletedOptionId);
    }
}
