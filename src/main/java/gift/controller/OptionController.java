package gift.controller;

import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.dto.WishResponse;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Option API", description = "상품 옵션 관련 API")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "상품 옵션 목록 조회", description = "특정 상품에 대한 모든 옵션을 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "옵션 목록 조회 성공"),
        @ApiResponse(responseCode = "-40401", description = "상품을 찾을 수 없음")
    })
    public ResponseEntity<Map<String, List<OptionResponse>>> getOptions(
        @PathVariable Long productId) {
        List<OptionResponse> options = optionService.getOptions(productId);
        return ResponseEntity.ok(Map.of("options", options));
    }

    @PostMapping
    @Operation(summary = "상품 옵션 추가", description = "상품에 옵션을 추가한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "옵션 추가 성공"),
        @ApiResponse(responseCode = "-40001", description = "옵션 유효성 검사 실패"),
        @ApiResponse(responseCode = "-40401", description = "해당 상품을 찾을 수 없음"),
        @ApiResponse(responseCode = "-40904", description = "옵션 이름이 이미 존재함")
    })
    public ResponseEntity<Map<String, OptionResponse>> addOption(@PathVariable Long productId,
        @Validated @RequestBody OptionRequest optionRequest) {
        OptionResponse optionResponse = optionService.addOption(productId, optionRequest);
        Map<String, OptionResponse> response = new HashMap<>();
        response.put("option", optionResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{optionId}")
    @Operation(summary = "상품 옵션 수정", description = "기존 상품 옵션의 정보를 수정한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "옵션 수정 성공",
            content = {@Content(schema = @Schema(implementation = OptionResponse.class))}),
        @ApiResponse(responseCode = "-40001", description = "옵션 유효성 검사 실패"),
        @ApiResponse(responseCode = "-40401", description = "해당 상품을 찾을 수 없음"),
        @ApiResponse(responseCode = "-40404", description = "해당 옵션이 존재하지 않음"),
        @ApiResponse(responseCode = "-40405", description = "해당 옵션이 존재하지만, 상품 옵션에 속하지 않음"),
        @ApiResponse(responseCode = "-40904", description = "옵션 이름이 이미 존재함")
    })
    public ResponseEntity<Void> updateOption(@PathVariable Long productId,
        @PathVariable Long optionId, @Validated @RequestBody OptionRequest optionRequest) {
        optionService.updateOption(productId, optionId, optionRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{optionId}")
    @Operation(summary = "상품 옵션 삭제", description = "기존 제품 옵션을 삭제한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "옵션 삭제 성공"),
        @ApiResponse(responseCode = "-40002", description = "상품의 옵션이 하나 이상이 아님"),
        @ApiResponse(responseCode = "-40401", description = "해당 상품을 찾을 수 없음"),
        @ApiResponse(responseCode = "-40404", description = "해당 옵션이 존재하지 않음"),
        @ApiResponse(responseCode = "-40405", description = "해당 옵션이 존재하지만, 상품 옵션에 속하지 않음")
    })
    public ResponseEntity<Void> deleteOption(@PathVariable Long productId,
        @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return ResponseEntity.noContent().build();
    }

}
