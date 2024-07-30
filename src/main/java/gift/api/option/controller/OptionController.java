package gift.api.option.controller;

import gift.api.option.dto.OptionRequest;
import gift.api.option.dto.OptionResponse;
import gift.api.option.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/{id}/options")
    @Operation(summary = "옵션 조회", description = "상품 옵션 조회")
    public ResponseEntity<List<OptionResponse>> getOptions(@PathVariable("id") Long productId) {
        return ResponseEntity.ok().body(optionService.getOptions(productId));
    }

    @PostMapping("/{id}/options")
    @Operation(summary = "옵션 추가", description = "상품 옵션 추가")
    public ResponseEntity<Void> addOption(@PathVariable("id") Long id,
        @RequestBody OptionRequest optionRequest) {
        optionService.add(id, optionRequest);
        return ResponseEntity.created(URI.create("/api/products/" + id)).build();
    }
}
