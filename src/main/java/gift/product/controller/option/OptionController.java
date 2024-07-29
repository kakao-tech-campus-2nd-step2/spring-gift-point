package gift.product.controller.option;

import gift.product.dto.option.OptionDto;
import gift.product.dto.option.OptionResponse;
import gift.product.model.Option;
import gift.product.service.OptionService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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

    @GetMapping("/options")
    public ResponseEntity<List<Option>> getOptionAll() {
        return ResponseEntity.ok(optionService.getOptionAll());
    }

    @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/options/{id}")
    public ResponseEntity<Option> getOption(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(optionService.getOption(id));
    }

    @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/products/{id}/options")
    public ResponseEntity<List<OptionResponse>> getOptionAllByProductId(
        @PathVariable(name = "id") Long productId) {
        return ResponseEntity.ok(optionService.getOptionAllByProductId(productId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/options/insert")
    public ResponseEntity<Option> insertOption(@Valid @RequestBody OptionDto optionDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(optionService.insertOption(optionDto));
    }

    @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping("/options/update/{id}")
    public ResponseEntity<Option> updateOption(@PathVariable(name = "id") Long id,
        @Valid @RequestBody OptionDto optionDto) {
        return ResponseEntity.ok(optionService.updateOption(id, optionDto));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/options/delete/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable(name = "id") Long id) {
        optionService.deleteOption(id);
        return ResponseEntity.ok().build();
    }

}
