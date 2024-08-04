package gift.controller;

import gift.dto.response.OptionResponse;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "[협업]OPTION API", description = "[협업] 옵션 컨트롤러")
public class OptionNewController {

    private final OptionService optionService;

    public OptionNewController(OptionService optionService) {
        this.optionService = optionService;

    }

    @GetMapping
    @Operation(summary = "옵션 조회 페이지", description = "옵션 목록을 조회합니다.")
    public ResponseEntity<List<OptionResponse>> getCategories(@PathVariable Long productId) {
        return ResponseEntity.ok(optionService.getOptionsByProductId(productId));
    }
}
