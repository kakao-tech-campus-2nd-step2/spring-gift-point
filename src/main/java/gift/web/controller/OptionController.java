package gift.web.controller;

import gift.domain.option.Option;
import gift.service.option.OptionService;
import gift.web.dto.OptionDto;
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
@RequestMapping("/api/products")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<OptionDto> createOption(@PathVariable Long productId, @RequestBody OptionDto optionDto) {
        return new ResponseEntity<>(optionService.createOption(productId, optionDto), HttpStatus.CREATED);
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionDto>> getOptionsByProductId(@PathVariable Long productId) {
        return new ResponseEntity<>(optionService.getOptionsByProductId(productId), HttpStatus.OK);
    }

    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<OptionDto> updateOption(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody OptionDto optionDto) {
        return new ResponseEntity<>(optionService.updateOption(optionId, productId, optionDto), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<String> deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(optionId, productId);
        return ResponseEntity.ok("Option deleted");
    }
}
