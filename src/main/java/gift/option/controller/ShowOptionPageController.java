package gift.option.controller;

import gift.common.exception.ProductNotFoundException;
import gift.option.domain.Option;
import gift.option.domain.OptionRequest;
import gift.option.service.OptionService;
import gift.product.model.Product;
import gift.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/products")
public class ShowOptionPageController {
    private final OptionService optionService;
    private final ProductService productService;

    public ShowOptionPageController(OptionService optionService, ProductService productService) {
        this.optionService = optionService;
        this.productService = productService;
    }

    // 옵션 메인 페이지 반환
    @GetMapping("/{id}/options")
    public String showOptionsForm(@PathVariable("id") Long productId, Model model) {
        Product product = productService.getProductById(productId);
        List<Option> options = optionService.getAllOptionByProduct(product);
        model.addAttribute("product", product);
        model.addAttribute("options", options);
        return "Option/option";
    }

    // 옵션 등록 페이지 반환
    @GetMapping("/{id}/options/new")
    public String showOptionCreateForm(@PathVariable("id") Long productId, Model model) {
        OptionRequest optionRequest = new OptionRequest();
        optionRequest.setProductId(productId);
        model.addAttribute("option", optionRequest);
        return "Option/create_option";
    }

    // 옵션 수정 페이지 반환
    @GetMapping("/{id}/options/edit/{option_id}")
    public String showOptionEditForm(@PathVariable("option_id") Long id, Model model) {
        Option option = optionService.getOptionById(id);
        if (option == null) {
            throw new ProductNotFoundException(id);
        }
        model.addAttribute("option", option);
        return "Option/edit_option";
    }
}
