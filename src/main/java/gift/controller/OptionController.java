package gift.controller;

import gift.controller.api.OptionApi;
import gift.dto.option.OptionRequest;
import gift.dto.option.OptionResponse;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController implements OptionApi {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<Void> addOption(@PathVariable Long productId, @Valid @RequestBody OptionRequest optionRequest) {
        var option = optionService.addOption(productId, optionRequest);
        return ResponseEntity.created(URI.create("/api/products/" + productId + "/options/" + option.id())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOption(@PathVariable Long productId, @PathVariable Long id, @Valid @RequestBody OptionRequest optionUpdateRequest) {
        optionService.updateOption(productId, id, optionUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptionResponse> getOption(@PathVariable Long productId, @PathVariable Long id) {
        var option = optionService.getOption(productId, id);
        return ResponseEntity.ok(option);
    }

    @GetMapping
    public ResponseEntity<List<OptionResponse>> getOptions(@PathVariable Long productId) {
        var options = optionService.getOptions(productId);
        return ResponseEntity.ok(options);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long productId, @PathVariable Long id) {
        optionService.deleteOption(productId, id);
        return ResponseEntity.noContent().build();
    }
}
