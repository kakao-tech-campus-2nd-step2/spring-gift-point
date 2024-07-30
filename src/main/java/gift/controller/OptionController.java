package gift.controller;

import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.service.OptionService;
import gift.vo.Option;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products/")
@Tag(name = "옵션 관리", description = "상품에 대한 옵션 조회, 추가에 관련된 API들을 제공합니다.")
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
    @Parameter(name = "id", description = "옵션 조회할 상품의 ID", required = true, example = "1")
    public ResponseEntity<List<OptionResponseDto>> getOption(@PathVariable Long id) {
        List<Option> allOptions = optionService.getOptionsPerProduct(id);

        List<OptionResponseDto> allOptionsDtos = allOptions.stream()
                .map(OptionResponseDto::toOptionResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(allOptionsDtos);
    }

    @Parameter(name = "optionRequestDtos", description = "추가할 옵션의 리스트", required = true)
    @PostMapping("/options")
    @Operation(
            summary = "옵션 추가",
            description = "상품에 대해 새로운 옵션을 추가하는 API입니다."
    )
    @Parameter(name = "optionRequestDtos", description = "상품에 추가할 옵션 Dto 리스트", required = true, example = "1")
    public ResponseEntity<Void> addOption(@Valid @RequestBody List<OptionRequestDto> optionRequestDtos) {
        optionService.addOption(optionRequestDtos);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
