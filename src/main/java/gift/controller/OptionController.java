package gift.controller;

import gift.dto.OptionRequestDTO;
import gift.dto.OptionsPageResponseDTO;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/product/options")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/{id}")
    public String getOptions(@PathVariable(name = "id") Long productId, Model model, @PageableDefault(size = 3) Pageable pageable) {
        OptionsPageResponseDTO options = optionService.getProductOptions(productId, pageable);

        model.addAttribute("options", options);
        model.addAttribute("productId", productId);

        return "option";
    }

    @PostMapping("/{id}")
    public String addOption(@PathVariable(name = "id") Long productId, Model model,
                          @Valid @RequestBody OptionRequestDTO optionRequestDTO, @PageableDefault(size = 3) Pageable pageable) {

        optionService.createOption(productId, optionRequestDTO);

        return "redirect:/api/product/options/" + productId;
    }

}
