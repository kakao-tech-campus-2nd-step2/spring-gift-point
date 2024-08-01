package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.option.*;
import gift.service.OptionService;
import gift.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OptionController {
    private final OptionService optionService;
    private final ProductService productService;

    @PostMapping("/api/products/{productId}/options")
    @ResponseStatus(HttpStatus.CREATED)
    public OptionResponseDTO addOption(@PathVariable int productId, @RequestBody SaveOptionDTO saveOptionDTO) {
        return optionService.saveOption(productId, saveOptionDTO);
    }

    @DeleteMapping("/api/products/{productId}/options/{optionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OptionResponseDTO deleteOption(@PathVariable int productId, @PathVariable int optionId) {
        return optionService.deleteOption(productId, optionId);
    }

    @PutMapping("/api/products/{productId}/options/{optionId}")
    public OptionResponseDTO updateOption(@PathVariable int productId, @PathVariable int optionId, @RequestBody SaveOptionDTO saveOptionDTO) {
        return optionService.updateOption(productId, optionId, saveOptionDTO);
    }

    @GetMapping("/api/products/{productId}/options")
    public List<OptionResponseDTO> getOptions(@PathVariable int productId) {
        return optionService.getOptions(productId);
    }

    @PostMapping("/api/products/{productId}/options/{optionId}")
    public OptionResponseDTO refill(@PathVariable int productId, @PathVariable int optionId, @RequestBody OptionQuantityDTO optionQuantityDTO) {
        return optionService.refillQuantity(productId, optionId, optionQuantityDTO);
    }
}
