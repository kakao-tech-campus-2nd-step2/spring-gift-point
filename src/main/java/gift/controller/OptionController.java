package gift.controller;

import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
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
@RequestMapping("/api")
public class OptionController {

    private OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/products/{productId}/options")
    public ResponseEntity<List<OptionResponseDto>> getOptionsByProductId(@PathVariable Long productId) {
        List<OptionResponseDto> optionList = optionService.getOptionsByProductId(productId);
        return new ResponseEntity<>(optionList, HttpStatus.OK);
    }

    @PostMapping("/products/{productId}/options")
    public ResponseEntity<String> addOptionToProduct(@PathVariable Long productId, @Valid @RequestBody OptionRequestDto optionRequestDto) {
        optionService.addOptionToProduct(productId, optionRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/options/{optionId}/subtract")
    public ResponseEntity<String> subtractOptionQuantity(@PathVariable Long optionId, @RequestParam Long quantity) {
        optionService.subtractOptionQuantity(optionId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}