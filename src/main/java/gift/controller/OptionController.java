package gift.controller;

import gift.dto.OptionDTO;
import gift.entity.Option;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    public ResponseEntity<List<Option>> getOptions(@PathVariable Long productId) {
        List<Option> options = optionService.getOptionsByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @PostMapping
    public ResponseEntity<Option> addOptionToProduct(@PathVariable Long productId, @Valid @RequestBody OptionDTO optionDTO) {
        Option option = optionService.addOptionToProduct(productId, optionDTO);
        return ResponseEntity.ok(option);
    }
}
