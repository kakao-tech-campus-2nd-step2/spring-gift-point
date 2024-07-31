package gift.controller;

import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getOptions(@PathVariable Long productId) {
        try {
            List<OptionResponse> options = optionService.findByProductId(productId);
            return ResponseEntity.ok(options);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOption(@RequestBody OptionRequest optionRequest) {
        OptionResponse savedOption = optionService.save(optionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<?> deleteOption(@PathVariable Long optionId) {
        try {
            optionService.delete(optionId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Option not found.");
        }
    }
}
