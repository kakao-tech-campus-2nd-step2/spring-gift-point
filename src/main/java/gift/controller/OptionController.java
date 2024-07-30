package gift.controller;

import gift.controller.api.OptionApi;
import gift.dto.option.OptionAddRequest;
import gift.dto.option.OptionResponse;
import gift.dto.option.OptionUpdateRequest;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/options")
public class OptionController implements OptionApi {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<Void> addOption(@Valid @RequestBody OptionAddRequest optionAddRequest) {
        var option = optionService.addOption(optionAddRequest);
        return ResponseEntity.created(URI.create("/api/options/" + option.id())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOption(@PathVariable Long id, @Valid @RequestBody OptionUpdateRequest optionUpdateRequest) {
        optionService.updateOption(id, optionUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<OptionResponse>> getOptions(@RequestParam Long productId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        var options = optionService.getOptions(productId, pageable);
        return ResponseEntity.ok(options);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        optionService.deleteOption(id);
        return ResponseEntity.noContent().build();
    }
}
