package gift.adminController;

import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.service.OptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/products/{productId}/options")
public class OptionAdminController {

    private final OptionService optionService;

    public OptionAdminController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    public String showOptionsPage(@PathVariable("productId") Long productId, Model model) {
        List<OptionResponse> options = optionService.getOptions(productId);
        model.addAttribute("options", options);
        model.addAttribute("productId", productId);
        return "optionAdmin/admin-options";
    }

    @GetMapping("/create")
    public String showCreateOptionPage(@PathVariable("productId") Long productId, Model model) {
        model.addAttribute("option", new OptionRequest());
        model.addAttribute("productId", productId);
        return "optionAdmin/option-create";
    }

    @PostMapping
    public String createOption(@PathVariable("productId") Long productId, Model model,
                               @Valid @ModelAttribute("option") OptionRequest optionRequest,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
        	model.addAttribute("productId", productId);
            return "optionAdmin/option-create";
        }
        optionService.addOption(productId, optionRequest, bindingResult);
        return "redirect:/admin/products/" + productId + "/options";
    }
}
