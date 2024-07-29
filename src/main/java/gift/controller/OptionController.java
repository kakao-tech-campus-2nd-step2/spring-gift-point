package gift.controller;

import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.service.OptionService;
import gift.vo.Option;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products/")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    /**
     *
     * @param id ProductId
     * @return List<Option>
     */
    @GetMapping("/{id}/options")
    @Operation(
            summary = "상품 옵션 조회",
            description = "주어진 상품 ID에 해당하는 모든 옵션을 조회하는 API입니다."
    )
    public ResponseEntity<List<OptionResponseDto>> getOption(@PathVariable Long id) {
        List<Option> allOptions = optionService.getOptionsPerProduct(id);

        List<OptionResponseDto> allOptionsDtos = allOptions.stream()
                .map(OptionResponseDto::toOptionResponseDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(allOptionsDtos, HttpStatus.OK);
    }

    @PostMapping("/options")
    @Operation(
            summary = "옵션 추가",
            description = "상품에 대해 새로운 옵션을 추가하는 API입니다."
    )
    public ResponseEntity<Void> addOption(@Valid @RequestBody List<OptionRequestDto> optionRequestDtos) {
        optionService.addOption(optionRequestDtos);
        return ResponseEntity.noContent().build();
    }

}
