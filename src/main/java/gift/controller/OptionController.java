package gift.controller;

import gift.dto.OptionQuantityRequest;
import gift.dto.OptionRequest;
import gift.dto.ResponseMessage;
import gift.entity.Option;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Option Management", description = "APIs for managing product options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "옵션 추가", description = "새로운 옵션을 추가합니다.",
        responses = @ApiResponse(responseCode = "201", description = "옵션 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Option.class))))
    public ResponseEntity<Option> addOption(@PathVariable("productId") Long productId,
        @Valid @RequestBody OptionRequest request) {
        Option option = optionService.addOption(productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(option);
    }

    @GetMapping("/options")
    @Operation(summary = "모든 옵션 조회", description = "모든 옵션을 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "옵션 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Option.class))))
    public List<Option> getAllOptions() {
        return optionService.getAllOptions();
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "상품 ID로 옵션 조회", description = "상품 ID를 통해 모든 옵션을 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "옵션 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Option.class))))
    public List<Option> getAllOptionsByProductId(@PathVariable("productId") Long productId) {
        return optionService.getAllOptionsByProductId(productId);
    }

    @GetMapping("/options/{optionId}")
    @Operation(summary = "옵션 ID로 옵션 조회", description = "옵션 ID에 해당하는 옵션을 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "옵션 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Option.class))))
    public Option getOptionById(@PathVariable("optionId") Long optionId) {
        return optionService.getOneOptionById(optionId);
    }

    @PutMapping("/{productId}/options/{optionId}")
    @Operation(summary = "옵션 업데이트", description = "상품 ID와 옵션 ID에 해당하는 옵션을 업데이트합니다.",
        responses = @ApiResponse(responseCode = "200", description = "옵션 업데이트 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Option.class))))
    public ResponseEntity<Option> updateOption(@PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId, @Valid @RequestBody OptionRequest request) {
        Option option = optionService.updateOption(productId, optionId, request);
        return ResponseEntity.ok(option);
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    @Operation(summary = "옵션 삭제", description = "상품 ID와 옵션 ID에 해당하는 옵션을 삭제합니다.",
        responses = @ApiResponse(responseCode = "200", description = "옵션 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))))
    public ResponseEntity<ResponseMessage> deleteOption(@PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId) {
        optionService.deleteOption(productId, optionId);
        ResponseMessage responseMessage = new ResponseMessage("삭제되었습니다.");
        return ResponseEntity.ok(responseMessage);
    }

    @PutMapping("/{productId}/options/{optionId}/sub")
    @Operation(summary = "옵션 수량 감소", description = "상품 ID와 옵션 ID에 해당하는 옵션 수량을 감소시킵니다.",
        responses = @ApiResponse(responseCode = "200", description = "옵션 수량 감소 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Option.class))))
    public ResponseEntity<Option> subtractOptionQuantity(@PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId, @RequestBody OptionQuantityRequest request) {
        Option option = optionService.subtractOptionQuantity(productId, optionId, request);
        return ResponseEntity.ok(option);
    }
}
