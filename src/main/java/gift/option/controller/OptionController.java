package gift.option.controller;

import gift.global.exception.BusinessException;
import gift.global.exception.DomainValidationException;
import gift.global.response.ErrorResponseDto;
import gift.global.response.ResultCode;
import gift.global.response.ResultResponseDto;
import gift.global.response.SimpleResultResponseDto;
import gift.global.utils.ResponseHelper;
import gift.option.domain.Option;
import gift.option.dto.OptionListResponseDto;
import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.exception.OptionValidException;
import gift.option.service.OptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("")
    public ResponseEntity<ResultResponseDto<OptionListResponseDto>> getAllOptions() {
        OptionListResponseDto optionListResponseDto = optionService.getAllOptions();
        return ResponseHelper.createResponse(ResultCode.GET_ALL_OPTIONS_SUCCESS, optionListResponseDto);
    }

    @GetMapping("/products/{product_id}")
    public ResponseEntity<ResultResponseDto<OptionListResponseDto>> getOptionsByProductId(@PathVariable(name = "product_id") Long productId) {
        OptionListResponseDto optionListResponseDto = optionService.getOptionsByProductId(productId);
        return ResponseHelper.createResponse(ResultCode.GET_OPTIONS_BY_PRODUCT_ID_SUCCESS, optionListResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponseDto<OptionResponseDto>> getOptionById(@PathVariable(name = "id") Long id) {
        OptionResponseDto optionResponseDto = optionService.getOptionById(id);
        return ResponseHelper.createResponse(ResultCode.GET_OPTION_BY_ID_SUCCESS, optionResponseDto);
    }

    @PostMapping("")
    public ResponseEntity<SimpleResultResponseDto> createOption(@RequestBody OptionRequestDto optionRequestDto) {
        optionService.createOption(optionRequestDto.toOptionServiceDto());
        return ResponseHelper.createSimpleResponse(ResultCode.CREATE_OPTION_SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> updateOption(@PathVariable(name = "id") Long id, @RequestBody OptionRequestDto optionRequestDto) {
        optionService.updateOption(optionRequestDto.toOptionServiceDto(id));
        return ResponseHelper.createSimpleResponse(ResultCode.UPDATE_OPTION_SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> deleteOption(@PathVariable(name = "id") Long id) {
        optionService.deleteOption(id);
        return ResponseHelper.createSimpleResponse(ResultCode.DELETE_OPTION_SUCCESS);
    }

    // GlobalException Handler 에서 처리할 경우,
    // RequestBody에서 발생한 에러가 HttpMessageNotReadableException 로 Wrapping 이 되는 문제가 발생한다
    // 때문에, 해당 에러로 Wrapping 되기 전 Controller 에서 Domain Error 를 처리해주었다
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(DomainValidationException e) {
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}
