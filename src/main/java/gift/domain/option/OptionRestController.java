package gift.domain.option;

import gift.domain.option.dto.OptionRequestDTO;
import gift.domain.option.dto.OptionResponseDTO;
import gift.global.response.ResponseMaker;
import gift.global.response.ResultResponseDto;
import gift.global.response.SimpleResultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Option", description = "Option API")
public class OptionRestController {

    private final OptionService optionService;

    public OptionRestController(OptionService optionService) {
        this.optionService = optionService;
    }

    /**
     * 모든 옵션 목록 조회
     */
    @GetMapping("/options")
    @Operation(summary = "모든 옵션 조회")
    public ResponseEntity<ResultResponseDto<List<Option>>> getOptions() {
        List<Option> options = optionService.getOptions();
        return ResponseMaker.createResponse(HttpStatus.OK, "모든 옵션 조회 성공", options);
    }

    /**
     * 특정 상품의 옵션 목록 조회
     */
    @GetMapping("/products/{productId}/options")
    @Operation(summary = "특정 상품의 옵션 목록 조회")
    public ResponseEntity<ResultResponseDto<OptionResponseDTO>> getOptionsByProductId(
        @Parameter(description = "상품 ID") @PathVariable("productId") Long productId
    ) {
        List<Option> options = optionService.getOptionsByProductId(productId);
        OptionResponseDTO optionResponseDTO = new OptionResponseDTO(options);

        return ResponseMaker.createResponse(HttpStatus.OK, "해당 상품의 옵션 조회 성공", optionResponseDTO);
    }

    /**
     * 특정 상품에 옵션 추가
     */
    @PostMapping("/products/{productId}/options")
    @Operation(summary = "특정 상품에 옵션 추가")
    public ResponseEntity<SimpleResultResponseDto> addOption(
        @Parameter(description = "상품 ID") @PathVariable("productId") Long productId,
        OptionRequestDTO optionRequestDTO
    ) {
        optionService.addOptionToExistsProduct(productId, optionRequestDTO);

        return ResponseMaker.createSimpleResponse(HttpStatus.OK, "해당 상품에 옵션 추가 성공");
    }

    /**
     * 특정 상품의 옵션 수정
     */
    @PutMapping("/products/{productId}/options/{optionId}")
    @Operation(summary = "특정 상품의 옵션 수정")
    public ResponseEntity<SimpleResultResponseDto> updateOption(
        @Parameter(description = "상품 ID") @PathVariable("productId") Long productId,
        @Parameter(description = "옵션 ID") @PathVariable("optionId") Long optionId,
        @Valid @RequestBody OptionRequestDTO optionRequestDTO
    ) {
        optionService.updateOption(productId, optionId, optionRequestDTO);

        return ResponseMaker.createSimpleResponse(HttpStatus.OK, "해당 상품에 옵션 수정 성공");
    }


}
