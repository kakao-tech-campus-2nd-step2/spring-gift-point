package gift.product.controller.option;

import gift.product.dto.option.OptionDto;
import gift.product.dto.option.OptionResponse;
import gift.product.exception.ExceptionResponse;
import gift.product.model.Option;
import gift.product.service.OptionService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@Tag(name = "option", description = "상품 옵션 관련 API")
@RestController
@RequestMapping("/api")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "옵션 조회 성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OptionResponse.class))))
    })
    @GetMapping("/options")
    public ResponseEntity<List<OptionResponse>> getOptionAll() {
        return ResponseEntity.ok(optionService.getOptionAll());
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "옵션 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Option.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/options/{id}")
    public ResponseEntity<Option> getOption(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(optionService.getOption(id));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OptionResponse.class)))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/products/{productId}/options")
    public ResponseEntity<List<OptionResponse>> getOptionAllByProductId(
        @PathVariable(name = "productId") Long productId) {
        return ResponseEntity.ok(optionService.getOptionAllByProductId(productId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "옵션 추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/products/{productId}/options")
    public ResponseEntity<Void> insertOption(@PathVariable(name = "productId") Long productId,
        @Valid @RequestBody OptionDto optionDto) {
        optionService.insertOption(optionDto, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "옵션 수정 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/products/{productId}/options/{optionId}")
    public ResponseEntity<Void> updateOption(@PathVariable(name = "optionId") Long optionId,
        @PathVariable(name = "productId") Long productId,
        @Valid @RequestBody OptionDto optionDto) {
        optionService.updateOption(optionId, optionDto, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "옵션 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "옵션 삭제 실패 (존재하지 않는 ID)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/products/{productId}/options/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable(name = "optionId") Long optionId,
        @PathVariable(name = "productId") Long productId) {
        optionService.deleteOption(optionId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
