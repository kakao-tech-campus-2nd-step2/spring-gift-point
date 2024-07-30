package gift.controller;


import gift.domain.Option;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/products/{product_id}/options")
@Tag(name = "Option", description = "옵션 API")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "전체 옵션 조회", description = "해당 상품의 전체 옵션을 조회합니다.")
    public ResponseEntity<?> getAllOptions(@PathVariable("product_id") Long productId) {
        return ResponseEntity.ok(optionService.getAllOptionByProductId(productId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "단일 옵션 조회", description = "아이디로 해당 상품의 옵션을 조회합니다.")
    public ResponseEntity<?> getOptionById(
        @PathVariable("product_id") Long productId,
        @PathVariable("id") Long id) {
        return ResponseEntity.ok(optionService.getOptionByProductIdAndId(productId,id));
    }

    @PostMapping
    @Operation(summary = "옵션 추가", description = "해당 상품에 옵션을 추가합니다.")
    public ResponseEntity<?> addOption(
        @PathVariable("product_id") Long productId,
        @Valid @RequestBody Option option) {
        optionService.addOption(productId, option);
        return ResponseEntity.status(HttpStatus.CREATED).body("Option added");
    }

    @PutMapping("/{id}")
    @Operation(summary = "옵션 수정", description = "아이디로 옵션을 수정합니다.")
    public ResponseEntity<?> updateOption(
        @PathVariable("id") Long id,
        @Valid @RequestBody Option option) {
        optionService.updateOption(id, option);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "옵션 삭제", description = "아이디로 옵션을 삭제합니다.")
    public ResponseEntity<?> deleteOption(@PathVariable("id") Long id) {
        optionService.deleteOption(id);
        return ResponseEntity.ok().build();
    }

}
