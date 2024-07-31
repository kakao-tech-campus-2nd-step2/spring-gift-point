package gift.controller.option;

import gift.DTO.option.OptionRequest;
import gift.DTO.option.OptionResponse;
import gift.service.OptionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class OptionController {

    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionResponse>> getOptionsByProductId(
        @PathVariable("productId") Long productId
    ) {
        List<OptionResponse> options = optionService.getAllOptionsByProductId(productId);
        return ResponseEntity.status(HttpStatus.OK).body(options);
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<OptionResponse> addOption(
        @PathVariable("productId") Long productId,
        @RequestBody @Valid OptionRequest optionRequest
    ) {
        OptionResponse option = optionService.addOption(productId, optionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(option);
    }

    @PutMapping("/{productId}/options/{optionId}/decrement")
    public ResponseEntity<OptionResponse> decrementOptionQuantity(
        @PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId,
        @RequestParam("quantity") Long quantity
    ) {
        OptionResponse optionResponse =
            optionService.decrementOptionQuantity(optionId, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(optionResponse);
    }
}
