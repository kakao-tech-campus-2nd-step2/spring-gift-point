package gift.option.presentation;

import gift.option.application.OptionService;
import gift.option.presentation.request.OptionSubtractQuantityRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/options")
public class OptionController implements OptionApi{

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/{id}/subtract")
    public void subtractOptionQuantity(
            @PathVariable("id") Long id,
            @RequestBody OptionSubtractQuantityRequest request
    ) {
        optionService.subtractOptionQuantity(request.toCommand(id));
    }
}
