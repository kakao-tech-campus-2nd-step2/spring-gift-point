package gift.controller;

import gift.dto.optionsDTOs.AddOptionDTO;
import gift.dto.optionsDTOs.GetOptionDTO;
import gift.service.OptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/options")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/{productId}")
    public String option(@PathVariable Long productId, Model model){
        GetOptionDTO options = optionService.getOptionsForHtml(productId);
        model.addAttribute("options", options.GetOptionList());
        model.addAttribute("productId", productId);
        return "option";
    }

    @PostMapping("/add/{productId}")
    public String addOption(@PathVariable Long productId, @ModelAttribute AddOptionDTO addOptionDTO, Model model) {
        optionService.addOption(addOptionDTO, productId);
        GetOptionDTO options = optionService.getOptionsForHtml(productId);
        model.addAttribute("options", options.GetOptionList());
        model.addAttribute("productId", productId);
        return "option";
    }

    @PostMapping("/update/{productId}/{oldName}")
    public String updateOption(@PathVariable Long productId, @PathVariable String oldName, @RequestParam("newName") String newName, Model model) {
        optionService.updateOption(oldName, newName, productId);
        GetOptionDTO options = optionService.getOptionsForHtml(productId);
        model.addAttribute("options", options.GetOptionList());
        model.addAttribute("productId", productId);
        return "option";
    }

    @GetMapping("/delete/{productId}/{optionName}")
    public String deleteOption(@PathVariable Long productId, @PathVariable String optionName, Model model) {
        optionService.deleteOption(optionName, productId);
        GetOptionDTO options = optionService.getOptionsForHtml(productId);
        model.addAttribute("options", options.GetOptionList());
        model.addAttribute("productId", productId);
        return "option";
    }
}
