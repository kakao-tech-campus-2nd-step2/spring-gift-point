package gift.controller;

import gift.dto.option.OptionCreateRequest;
import gift.dto.option.OptionResponse;
import gift.dto.option.OptionUpdateRequest;
import gift.service.OptionService;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    public ResponseEntity<List<OptionResponse>> getOptionsByProductId(
        @PathVariable Long productId
    ) {
        List<OptionResponse> options = optionService.getOptionsByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @GetMapping("/{optionId}")
    public ResponseEntity<OptionResponse> getOptionById(
        @PathVariable Long productId,
        @PathVariable Long optionId
    ) {
        OptionResponse option = optionService.getOptionById(productId, optionId);
        return ResponseEntity.ok(option);
    }

    @PostMapping
    public ResponseEntity<OptionResponse> addOptionToProduct(
        @PathVariable Long productId,
        @Valid @RequestBody OptionCreateRequest optionCreateRequest
    ) {
        OptionResponse createdOption = optionService.addOptionToProduct(
            productId,
            optionCreateRequest
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOption);
    }

    @PutMapping("/{optionId}")
    public ResponseEntity<OptionResponse> updateOption(
        @PathVariable Long productId,
        @PathVariable Long optionId,
        @Valid @RequestBody OptionUpdateRequest optionUpdateRequest
    ) {
        OptionResponse updatedOption = optionService.updateOption(
            productId,
            optionId,
            optionUpdateRequest
        );
        return ResponseEntity.ok(updatedOption);
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(
        @PathVariable Long productId,
        @PathVariable Long optionId
    ) {
        optionService.deleteOption(productId, optionId);
        return ResponseEntity.noContent().build();
    }
}
