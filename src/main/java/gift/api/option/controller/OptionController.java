package gift.api.option.controller;

import gift.api.option.dto.OptionRequest;
import gift.api.option.dto.OptionResponse;
import gift.api.option.service.OptionService;
import gift.global.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
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
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Option", description = "Option API")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "옵션 조회", description = "상품 옵션 조회")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<ListResponse<OptionResponse>> getOptions(
        @Parameter(required = true, description = "옵션을 조회할 상품의 ID")
        @PathVariable("productId") Long productId) {

        return ResponseEntity.ok().body(ListResponse.of(optionService.getOptions(productId)));
    }

    @PostMapping
    @Operation(summary = "옵션 추가", description = "상품 옵션 추가")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "존재하지 않는 상품 ID\t\n\n유효하지 않은 옵션 이름"),
    })
    public ResponseEntity<Void> add(
        @Parameter(required = true, description = "옵션을 추가할 상품의 ID")
        @PathVariable("productId") Long id,
        @Parameter(required = true, description = "옵션 요청 본문")
        @RequestBody OptionRequest optionRequest) {

        optionService.add(id, optionRequest);
        return ResponseEntity.created(URI.create("/api/products/" + id)).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "옵션 수정", description = "상품 옵션 수정")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<Void> update(
        @Parameter(description = "옵션을 수정할 상품의 ID")
        @PathVariable Long productId,
        @Parameter(required = true, description = "수정할 옵션의 ID")
        @PathVariable Long id,
        @Parameter(required = true, description = "옵션 요청 본문")
        @RequestBody OptionRequest optionRequest) {

        optionService.update(id, optionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "옵션 삭제", description = "상품 옵션 삭제")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<Void> delete(
        @Parameter(description = "옵션을 삭제할 상품의 ID")
        @PathVariable Long productId,
        @Parameter(required = true, description = "삭제할 옵션의 ID")
        @PathVariable Long id) {

        optionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
