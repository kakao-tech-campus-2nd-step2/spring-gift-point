package gift.controller;

import gift.dto.optionsDTOs.AllOptionDto;
import gift.service.OptionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetOptionApiController {
    private final OptionService optionService;

    public GetOptionApiController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/api/products/{productId}/options")
    public List<AllOptionDto> getoptions(@PathVariable Long productId){
        List<AllOptionDto> options = optionService.getAllOptions(productId);
        return options;
    }
}
