package gift.controller;

import gift.service.OptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/products/{productId}/options")
public class OptionViewController {

    private final OptionService optionService;

    public OptionViewController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/addForm")
    public String getOptionAddForm(@PathVariable("productId") Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "optionAddForm";
    }

    /**
     * optionDto 객체에 productId가 포함되어있으므로 따로 productId를 포함시키지 않음.
     */
    @GetMapping("/{optionId}")
    public String getOptionEditForm(@PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId, Model model) {
        model.addAttribute("option", optionService.findOptionById(optionId));
        model.addAttribute("productId", productId);
        return "optionEditForm";
    }
}
