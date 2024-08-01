package gift.controller;

import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    public ResponseEntity<List<OptionResponse>> getOptions(@PathVariable Long productId) {
        List<OptionResponse> options = optionService.findByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @PostMapping
    public ResponseEntity<OptionResponse> addOption(@PathVariable Long productId, @RequestBody OptionRequest optionRequest) {
        optionRequest = new OptionRequest(optionRequest.name(), optionRequest.quantity(), productId);
        OptionResponse savedOption = optionService.save(optionRequest);
        return ResponseEntity.ok(savedOption);
    }
}
