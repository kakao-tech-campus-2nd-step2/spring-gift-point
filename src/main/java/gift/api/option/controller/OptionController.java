package gift.api.option.controller;

import gift.api.option.dto.OptionRequest;
import gift.api.option.dto.OptionResponse;
import gift.api.option.service.OptionService;
import gift.global.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
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
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "옵션 조회", description = "상품 옵션 조회")
    public ResponseEntity<ListResponse<OptionResponse>> getOptions(
        @PathVariable("productId") Long productId) {
        return ResponseEntity.ok().body(ListResponse.of(optionService.getOptions(productId)));
    }

    @PostMapping
    @Operation(summary = "옵션 추가", description = "상품 옵션 추가")
    public ResponseEntity<Void> add(@PathVariable("productId") Long id,
        @RequestBody OptionRequest optionRequest) {
        optionService.add(id, optionRequest);
        return ResponseEntity.created(URI.create("/api/products/" + id)).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "옵션 수정", description = "상품 옵션 수정")
    public ResponseEntity<Void> update(@PathVariable Long productId, @PathVariable Long id,
        @RequestBody OptionRequest optionRequest) {
        optionService.update(id, optionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "옵션 삭제", description = "상품 옵션 삭제")
    public ResponseEntity<Void> delete(@PathVariable Long productId, @PathVariable Long id) {
        optionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
