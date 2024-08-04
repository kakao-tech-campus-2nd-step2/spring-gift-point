package gift.controller.rest;

import gift.dto.option.OptionRequestDTO;
import gift.dto.option.OptionResponseDto;
import gift.dto.response.MessageResponseDTO;
import gift.entity.Option;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Option 컨트롤러", description = "Option API입니다.")
@RestController
@RequestMapping("/api/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @Operation(summary = "모든 옵션 조회", description = "모든 옵션을 조회합니다.")
    @GetMapping()
    public ResponseEntity<List<OptionResponseDto>> getAllOptions() {
        return ResponseEntity.ok().body(optionService.findAll());
    }

    @Operation(summary = "옵션 조회", description = "id로 옵션을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<OptionResponseDto> getOptionById(@PathVariable Long id) {
        Option option = optionService.findById(id);
        return ResponseEntity.ok().body(new OptionResponseDto(option));
    }

    @Operation(summary = "옵션 생성", description = "옵션을 생성합니다.")
    @PostMapping()
    public ResponseEntity<OptionResponseDto> createOption(@RequestBody @Valid OptionRequestDTO optionRequestDTO, HttpSession session) {
        String email = (String) session.getAttribute("email");
        Option option = optionService.save(optionRequestDTO, email);
        return ResponseEntity.ok().body(new OptionResponseDto(option));
    }

    @Operation(summary = "옵션 편집", description = "옵션을 편집합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<OptionResponseDto> updateOption(@PathVariable Long id, @RequestBody @Valid OptionRequestDTO optionRequestDTO, HttpSession session) {
        String email = (String) session.getAttribute("email");
        Option option = optionService.update(id, optionRequestDTO, email);
        return ResponseEntity.ok().body(new OptionResponseDto(option));
    }

    @Operation(summary = "옵션 삭제", description = "옵션을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteOption(@PathVariable Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        optionService.delete(id, email);
        return ResponseEntity.ok().body(new MessageResponseDTO("Option deleted successfully"));
    }
}
