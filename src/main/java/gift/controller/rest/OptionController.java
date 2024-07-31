package gift.controller.rest;

import gift.entity.MessageResponseDTO;
import gift.entity.Option;
import gift.entity.OptionDTO;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "옵션 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Option.class)))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @GetMapping()
    public ResponseEntity<List<Option>> getAllOptions() {
        return ResponseEntity.ok().body(optionService.findAll());
    }

    @Operation(summary = "옵션 조회", description = "id로 옵션을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "옵션 조회 성공",
                    content = @Content(schema = @Schema(implementation = Option.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "옵션 조회 실패",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Option> getOptionById(@PathVariable Long id) {
        return ResponseEntity.ok().body(optionService.findById(id));
    }

    @Operation(summary = "옵션 생성", description = "옵션을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "옵션 생성 성공",
                    content = @Content(schema = @Schema(implementation = Option.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @PostMapping()
    public ResponseEntity<Option> createOption(@RequestBody @Valid OptionDTO optionDTO, HttpSession session) {
        String email = (String) session.getAttribute("email");
        return ResponseEntity.ok().body(optionService.save(optionDTO, email));
    }

    @Operation(summary = "옵션 편집", description = "옵션을 편집합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "옵션 생성 성공",
                    content = @Content(schema = @Schema(implementation = Option.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "대상 옵션 조회 살패",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Option> updateOption(@PathVariable Long id, @RequestBody @Valid OptionDTO optionDTO, HttpSession session) {
        String email = (String) session.getAttribute("email");
        return ResponseEntity.ok().body(optionService.update(id, optionDTO, email));
    }

    @Operation(summary = "옵션 삭제", description = "옵션을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "옵션 생성 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "대상 옵션 조회 살패",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteOption(@PathVariable Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        optionService.delete(id, email);
        return ResponseEntity.ok().body(new MessageResponseDTO("Option deleted successfully"));
    }
}
