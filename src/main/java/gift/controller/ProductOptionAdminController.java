package gift.controller;

import gift.dto.OptionRequest;
import gift.entity.Option;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products/{productId}/options")
public class ProductOptionAdminController {

    private final OptionService optionService;

    public ProductOptionAdminController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/add")
    public String addOptionForm(@PathVariable Long productId, Model model) {
        model.addAttribute("optionRequest", new OptionRequest());
        model.addAttribute("productId", productId);
        return "option-form";
    }

    @PostMapping("/add")
    public String addOption(@PathVariable Long productId,
        @Valid @ModelAttribute("optionRequest") OptionRequest optionRequest,
        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", productId);
            return "option-form";
        }
        optionService.addOption(productId, optionRequest);
        return "redirect:/admin/products/edit/" + productId;
    }

    @GetMapping("/edit/{optionId}")
    public String editOptionForm(@PathVariable Long productId,
        @PathVariable Long optionId, Model model) {
        Option option = optionService.getOneOptionById(optionId);
        model.addAttribute("optionRequest",
            new OptionRequest(option.getName(), option.getQuantity()));
        model.addAttribute("productId", productId);
        model.addAttribute("optionId", optionId);
        return "option-form";
    }

    @PostMapping("/edit/{optionId}")
    public String editOption(@PathVariable Long productId,
        @PathVariable Long optionId,
        @Valid @ModelAttribute("optionRequest") OptionRequest optionRequest,
        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", productId);
            model.addAttribute("optionId", optionId);
            return "option-form";
        }
        optionService.updateOption(productId, optionId, optionRequest);
        return "redirect:/admin/products/edit/" + productId;
    }

    @GetMapping("/delete/{optionId}")
    public String deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return "redirect:/admin/products/edit/" + productId;
    }
}
