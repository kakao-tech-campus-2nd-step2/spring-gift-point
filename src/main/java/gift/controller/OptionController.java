package gift.controller;

import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
@Tag(name = "Options", description = "옵션 관련 API")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    @Operation(summary = "옵션 추가", description = "새로운 옵션을 추가합니다.")
    public ResponseEntity<OptionResponseDto> addOption(@RequestBody OptionRequestDto optionRequestDto) {
        OptionResponseDto createdOption = optionService.addOption(optionRequestDto);
        return new ResponseEntity<>(createdOption, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "옵션 수정", description = "기존 옵션을 수정합니다.")
    public ResponseEntity<OptionResponseDto> updateOption(@PathVariable Long id, @RequestBody OptionRequestDto optionRequestDto) {
        OptionResponseDto updatedOption = optionService.updateOption(id, optionRequestDto);
        return new ResponseEntity<>(updatedOption, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "모든 옵션 조회", description = "모든 옵션을 조회합니다.")
    public ResponseEntity<List<OptionResponseDto>> getAllOptions() {
        List<OptionResponseDto> options = optionService.getAllOptions();
        return new ResponseEntity<>(options, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "옵션 조회", description = "ID로 옵션을 조회합니다.")
    public ResponseEntity<OptionResponseDto> getOptionById(@PathVariable Long id) {
        OptionResponseDto option = optionService.getOptionById(id);
        return new ResponseEntity<>(option, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "옵션 삭제", description = "기존 옵션을 삭제합니다.")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        optionService.deleteOption(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
