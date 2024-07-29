package gift.product.controller;

import gift.product.dto.OptionDTO;
import gift.product.service.OptionService;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product/{productId}/option")
public class AdminOptionController {

    private final OptionService optionService;
    private final ProductService productService;

    @Autowired
    public AdminOptionController(
        OptionService optionService,
        ProductService productService
    ) {
        this.optionService = optionService;
        this.productService = productService;
    }

    @GetMapping
    public String getAllOptions(@PathVariable Long productId, Model model, Pageable pageable) {
        System.out.println("[AdminOptionController] getAllOptions()");
        model.addAttribute("optionList", optionService.getAllOptions(productId, pageable));
        model.addAttribute("product", productService.findById(productId));
        return "product-option";
    }

    @GetMapping("/register")
    public String showOptionRegisterForm(@PathVariable Long productId, Model model) {
        System.out.println("[AdminOptionController] showOptionRegisterForm()");
        model.addAttribute("option", new OptionDTO());
        return "product-option-form";
    }

    @PostMapping
    public String registerOption(@PathVariable Long productId, @Valid @ModelAttribute OptionDTO optionDTO, BindingResult bindingResult, Model model) {
        System.out.println("[AdminOptionController] registerOption()");
        if (bindingResult.hasErrors()) {
            model.addAttribute("option", optionDTO);
            return "product-option-form";
        }
        optionService.registerOption(productId, optionDTO);
        return "redirect:/admin/product/" + productId + "/option";
    }

    @GetMapping("/{optionId}")
    public String showOptionUpdateForm(@PathVariable Long productId, @PathVariable Long optionId, Model model) {
        System.out.println("[AdminOptionController] showOptionUpdateForm()");
        model.addAttribute("option", optionService.findById(optionId));
        return "product-option-update-form";
    }

    @PutMapping("/{optionId}")
    public String updateOption(@PathVariable Long optionId, @PathVariable Long productId, @Valid @ModelAttribute OptionDTO optionDTO, BindingResult bindingResult, Model model) {
        System.out.println("[AdminOptionController] updateOption()");
        if(bindingResult.hasErrors()) {
            model.addAttribute("option", optionDTO);
            return "product-option-update-form";
        }
        optionService.updateOption(optionId, productId, optionDTO);
        return "redirect:/admin/product/" + productId + "/option";
    }

}
