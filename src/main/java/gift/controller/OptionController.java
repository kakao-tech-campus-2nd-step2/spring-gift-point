package gift.controller;

import gift.dto.OptionDTO;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @Operation(summary = "상품 ID로 옵션 가져오기")
    @GetMapping
    public ResponseEntity<List<OptionDTO>> getOptionsByProductId(@PathVariable Long productId) {
        List<OptionDTO> options = optionService.getOptionsByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "새 옵션 생성")
    @PostMapping
    public ResponseEntity<OptionDTO> createOption(@PathVariable Long productId, @Valid @RequestBody OptionDTO optionDTO) {
        OptionDTO savedOption = optionService.createOption(productId, optionDTO);
        return ResponseEntity.created(URI.create("/api/products/" + productId + "/options/" + savedOption.getId())).body(savedOption);
    }
}
