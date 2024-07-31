package gift.product.option.controller;

import gift.product.option.dto.request.CreateOptionRequest;
import gift.product.option.dto.request.UpdateOptionRequest;
import gift.product.option.dto.response.OptionResponse;
import gift.product.option.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/products/{product_id}/options")
@Tag(name = "상품 옵션 관리", description = "{product_id} 상품의 옵션을 관리하는 API")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "상품의 모든 옵션 조회", description = "상품이 가지고 있는 모든 옵션을 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "상품이 존재하지 않음")
    })
    public ResponseEntity<List<OptionResponse>> getOptions(
        @PathVariable("product_id") Long productId) {
        return ResponseEntity.ok(optionService.getProductOptions(productId));
    }

    @PostMapping
    @Operation(summary = "옵션 추가", description = "상품에 옵션을 추가합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "성공",
            headers = {@Header(name = "location", description = "옵션 생성 위치 엔드포인트")}),
        @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 오류 발생"),
        @ApiResponse(responseCode = "404", description = "상품이 존재하지 않음"),
        @ApiResponse(responseCode = "409", description = "해당 상품에 동일한 이름의 옵션이 존재함"),
    })
    public ResponseEntity<OptionResponse> addOption(@PathVariable("product_id") Long productId,
        @RequestBody @Valid CreateOptionRequest request) {
        OptionResponse response = optionService.createOption(productId, request);
        URI location = UriComponentsBuilder.fromPath("/api/products/{productId}/options/{id}")
            .buildAndExpand(productId, response.id())
            .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "옵션 수정", description = "{id} 옵션을 수정합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 오류 발생"),
        @ApiResponse(responseCode = "404", description = "상품 또는 옵션이 존재하지 않음"),
        @ApiResponse(responseCode = "409", description = "해당 상품에 동일한 이름의 옵션이 존재함"),
    })
    public ResponseEntity<Void> updateOption(@PathVariable("product_id") Long productId,
        @PathVariable("id") Long id, @RequestBody @Valid UpdateOptionRequest request) {
        optionService.updateOption(productId, id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    @Operation(summary = "옵션 상품", description = "{id} 옵션을 삭제합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "성공"),
        @ApiResponse(responseCode = "400", description = "해당 옵션이 상품의 마지막 옵션이므로 삭제할 수 없음"),
        @ApiResponse(responseCode = "404", description = "옵션이 존재하지 않음")
    })
    public ResponseEntity<Void> deleteOption(@PathVariable("product_id") Long productId,
        @PathVariable("id") Long id) {
        optionService.deleteOption(productId, id);
        return ResponseEntity.ok().build();
    }
}
