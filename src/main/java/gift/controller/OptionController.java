package gift.controller;

import gift.dto.optionDto.OptionDto;
import gift.dto.optionDto.OptionResponseDto;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/products/{productId}/options")
@RestController
@Tag(name = "Option Management", description = "Option Management API")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService){
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "상품의 전체 옵션 호출", description = "상품에 등록된 옵션을 불러올 때 사용하는 API")
    public ResponseEntity<List<OptionResponseDto>> getAllOptionsById(@PathVariable Long productId) {
        List<OptionResponseDto> options = optionService.getAllOptionsById(productId);
        return ResponseEntity.ok().body(options);
    }

    @PostMapping
    @Operation(summary = "새로운 옵션 추가", description = "새로운 옵션을 추가할 때 사용하는 API")
    public ResponseEntity<OptionResponseDto> addNewOption(@PathVariable Long productId, @RequestBody OptionDto optionDto) {
        OptionResponseDto optionResponseDto = optionService.addNewOption(productId, optionDto);
        return ResponseEntity.ok().body(optionResponseDto);
    }

    @PutMapping("/{optionId}")
    @Operation(summary = "옵션 정보 수정", description = "옵션을 수정할 때 사용하는 API")
    public ResponseEntity<OptionResponseDto> updateOption(@PathVariable Long optionId, @RequestBody OptionDto optionDto) {
        OptionResponseDto optionResponseDto = optionService.updateOption(optionId,optionDto);
        return ResponseEntity.ok().body(optionResponseDto);
    }

    @DeleteMapping("/{optionId}")
    @Operation(summary = "등록된 옵션 정보 삭제", description = "옵션을 삭제할 때 사용하는 API")
    public ResponseEntity<Void> deleteOption(@PathVariable Long optionId){
        optionService.deleteOption(optionId);
        return ResponseEntity.ok().build();
    }
}
