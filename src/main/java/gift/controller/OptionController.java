package gift.controller;

import gift.dto.OptionDTO;
import gift.model.Option;
import gift.model.Product;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products/{productId}/options")
@Tag(name = "옵션 관리 API", description = "옵션 관리를 위한 API")
public class OptionController {

    private final OptionService optionService;
    private final ProductService productService;

    public OptionController(OptionService optionService, ProductService productService) {
        this.optionService = optionService;
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "옵션 목록 얻기", description = "해당 상품의 옵션들을 조회합니다.")
    public String listOptions(@PathVariable Long productId, Model model) {
        Product product = productService.findProductsById(productId);
        List<Option> options = optionService.findAllByProductId(productId);
        model.addAttribute("product", product);
        model.addAttribute("options", options);
        return "option_list";
    }

    @GetMapping("/new")
    @Operation(summary = "옵션 추가 폼 보기", description = "옵션을 추가할 수 있는 폼으로 이동합니다.")
    public String showAddOptionForm(@PathVariable Long productId, Model model) {
        model.addAttribute("optionDTO", new OptionDTO("", null, productId));
        return "add_option_form";
    }

    @PostMapping
    @Operation(summary = "옵션 추가", description = "새로운 옵션을 추가합니다.")
    public String addOption(@PathVariable Long productId,
        @ModelAttribute @Valid OptionDTO optionDTO,
        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("optionDTO", optionDTO);
            return "add_option_form";
        }
        optionService.saveOption(optionDTO);
        return "redirect:/admin/products/" + productId + "/options";
    }

    @GetMapping("/{optionId}")
    @Operation(summary = "옵션 수정 폼 보기", description = "옵션을 수정할 수 있는 폼으로 이동합니다.")
    public String showEditOptionForm(@PathVariable Long productId, @PathVariable Long optionId,
        Model model) {
        Option option = optionService.findOptionById(optionId);
        model.addAttribute("optionDTO", OptionService.toDTO(option));
        model.addAttribute("optionId", optionId);
        return "edit_option_form";
    }

    @PutMapping("/{optionId}")
    @Operation(summary = "옵션 수정", description = "옵션을 수정합니다.")
    public String editOption(@PathVariable Long productId, @PathVariable Long optionId,
        @ModelAttribute @Valid OptionDTO optionDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("optionId", optionId);
            return "edit_option_form";
        }
        optionService.updateOption(optionDTO, optionId);
        return "redirect:/admin/products/" + productId + "/options";
    }

    @DeleteMapping("/{optionId}")
    @Operation(summary = "옵션 삭제", description = "옵션을 삭제합니다.")
    public String deleteOption(@PathVariable Long productId, @PathVariable Long optionId,
        Model model) {
        try {
            optionService.deleteOption(optionId, productId);
        } catch (IllegalArgumentException e) {
            model.addAttribute("deleteError", e.getMessage());
            return listOptions(productId, model);
        }
        return "redirect:/admin/products/" + productId + "/options";
    }
}
