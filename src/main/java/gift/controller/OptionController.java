package gift.controller;

import gift.dto.ErrorResponseDto;
import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Option", description = "옵션 관리 API")
public class OptionController {

    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @Operation(summary = "상품 옵션 조회", description = "상품 ID에 따라 해당 상품의 모든 옵션을 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 옵션 조회 성공", content = @Content(schema = @Schema(implementation = OptionResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionResponseDto>> getOptionsByProductId(
        @PathVariable Long productId) {
        List<OptionResponseDto> optionList = optionService.getOptionsByProductId(productId);
        return new ResponseEntity<>(optionList, HttpStatus.OK);
    }

    @Operation(summary = "상품 옵션 추가", description = "상품 ID에 따라 상품에 새로운 옵션을 추가한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 옵션 추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 옵션명", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/{productId}/options")
    public ResponseEntity<String> addOptionToProduct(@PathVariable Long productId,
        @RequestBody @Valid OptionRequestDto optionRequestDto) {
        optionService.addOptionToProduct(productId, optionRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "상품 옵션 삭제", description = "상품 ID와 옵션 ID에 따라 해당 옵션을 삭제한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 옵션 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "상품 또는 옵션을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<String> deleteOption(@PathVariable Long productId,
        @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}