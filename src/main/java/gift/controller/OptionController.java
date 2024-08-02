package gift.controller;

import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "옵션이 성공적으로 추가되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = OptionResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 예: 옵션 이름이 유효하지 않은 경우.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<OptionResponseDto> addOption(
            @Parameter(description = "추가할 옵션의 정보", required = true) @RequestBody OptionRequestDto optionRequestDto) {
        OptionResponseDto createdOption = optionService.addOption(optionRequestDto);
        return new ResponseEntity<>(createdOption, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "옵션 수정", description = "기존 옵션을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "옵션이 성공적으로 수정되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = OptionResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "옵션을 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<OptionResponseDto> updateOption(
            @Parameter(description = "수정할 옵션 ID", required = true) @PathVariable Long id,
            @Parameter(description = "수정할 옵션의 정보", required = true) @RequestBody OptionRequestDto optionRequestDto) {
        OptionResponseDto updatedOption = optionService.updateOption(id, optionRequestDto);
        return new ResponseEntity<>(updatedOption, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "모든 옵션 조회", description = "모든 옵션을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "옵션 목록이 성공적으로 반환되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = OptionResponseDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<List<OptionResponseDto>> getAllOptions() {
        List<OptionResponseDto> options = optionService.getAllOptions();
        return new ResponseEntity<>(options, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "옵션 조회", description = "ID로 옵션을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "옵션이 성공적으로 반환되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = OptionResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "옵션을 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<OptionResponseDto> getOptionById(
            @Parameter(description = "조회할 옵션 ID", required = true) @PathVariable Long id) {
        OptionResponseDto option = optionService.getOptionById(id);
        return new ResponseEntity<>(option, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "옵션 삭제", description = "기존 옵션을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "옵션이 성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "옵션을 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteOption(
            @Parameter(description = "삭제할 옵션 ID", required = true) @PathVariable Long id) {
        optionService.deleteOption(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
