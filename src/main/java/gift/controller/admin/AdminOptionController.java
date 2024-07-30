package gift.controller.admin;

import gift.controller.dto.request.OptionRequest;
import gift.controller.dto.response.OptionResponse;
import gift.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/product/option")
public class AdminOptionController {

    private final ProductService productService;

    public AdminOptionController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/new")
    public String newOption(Model model,
        @RequestParam("id") @NotNull @Min(1) Long id) {
        model.addAttribute("productId", id);
        return "option/newOption";
    }

    @GetMapping("/{id}")
    public String editOption(Model model,
        @PathVariable("id") @NotNull @Min(1) Long productId,
        @RequestParam("option-id") @NotNull @Min(1) Long optionId
    ) {
        OptionResponse.Info option = productService.findOptionById(productId, optionId);
        model.addAttribute("option", option);
        return "option/editOption";
    }

    @PostMapping("")
    public String createOption(
            @Valid @ModelAttribute OptionRequest.CreateOption request) {
        productService.addOption(request);
        return "redirect:/admin/product/" + request.productId();
    }

    @PutMapping("")
    public String updateOption(
            @Valid @ModelAttribute OptionRequest.UpdateOption request) {
        productService.updateOption(request);
        return "redirect:/admin/product/" + request.productId();
    }

    @DeleteMapping("/{id}/{option-id}")
    public ResponseEntity<Void> deleteOptionById(
            @PathVariable("id") @NotNull @Min(1) Long productId,
            @PathVariable("option-id") @NotNull @Min(1) Long optionId) {
        productService.deleteByIdAndOptionId(productId, optionId);
        return ResponseEntity.ok().build();
    }
}
