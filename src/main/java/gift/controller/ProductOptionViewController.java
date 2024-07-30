package gift.controller;

import gift.dto.OptionResponseDto;
import gift.dto.ProductOptionResponseDto;
import gift.service.OptionService;
import gift.service.ProductOptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductOptionViewController {

    private final OptionService optionService;
    private final ProductOptionService productOptionService;

    public ProductOptionViewController(OptionService optionService, ProductOptionService productOptionService) {
        this.optionService = optionService;
        this.productOptionService = productOptionService;
    }

    @GetMapping("/{productId}/options")
    public String getProductOptionsPage(@PathVariable Long productId, Model model) {
        List<ProductOptionResponseDto> productOptions = productOptionService.getProductOptions(productId);
        model.addAttribute("productId", productId);
        model.addAttribute("productOptions", productOptions);
        return "product_options";
    }

    @GetMapping("/{productId}/options/new")
    public String showProductOptionForm(@PathVariable Long productId, Model model) {
        List<OptionResponseDto> allOptions = optionService.getAllOptions();
        model.addAttribute("productId", productId);
        model.addAttribute("allOptions", allOptions);
        return "product_option_form";
    }
}
