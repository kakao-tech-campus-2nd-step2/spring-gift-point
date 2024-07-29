package gift.option;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class OptionManageController {
    private final OptionService optionService;

    public OptionManageController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/{productID}/options")
    public List<OptionResponse> getProductOptions(@PathVariable Long productID){
        return optionService.findAllProductOptions(productID);
    }

    @PostMapping("/{productID}/options")
    public List<OptionResponse> addProductOptions(@PathVariable Long productID, @Valid @RequestBody List<OptionRequest> optionRequests){
        return optionService.insertProductNewOptions(productID, optionRequests);
    }

    @PutMapping("/{productID}/options/{optionId}")
    public OptionResponse updateProductOption(@PathVariable Long productID, @PathVariable Long optionId, @Valid @RequestBody OptionRequest optionRequest){
        return optionService.updateOption(productID, optionId, optionRequest);
    }

    @DeleteMapping("/{productID}/options/{optionId}")
    public void deleteProductOption(@PathVariable Long productID, @PathVariable Long optionId){
        optionService.deleteOption(productID, optionId);
    }
}
