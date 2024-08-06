package gift.controller;

import gift.domain.option.OptionRequest;
import gift.domain.option.OptionResponse;
import gift.service.OptionService;
import jakarta.validation.Valid;
import java.util.List;
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
    public List<OptionResponse> readOptions(@PathVariable("productId") Long productId) {
        return optionService.findAllByProductId(productId);
    }

    @PostMapping
    public OptionResponse createOption(@PathVariable Long productId,
        @Valid @RequestBody OptionRequest optionRequest
    ) {
        return optionService.save(productId, optionRequest);
    }

    @PutMapping("/{optionId}")
    public OptionResponse updateOption(@PathVariable Long productId, @PathVariable Long optionId,
        @Valid @RequestBody OptionRequest optionRequest
    ) {
        return optionService.update(productId, optionId, optionRequest);
    }

    @DeleteMapping("/{optionId}")
    public void deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.delete(productId, optionId);
    }
}
