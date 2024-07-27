package gift.option.controller;

import gift.option.domain.OptionRequest;
import gift.option.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/options")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    // 옵션 등록 처리
    @PostMapping
    public String createOption(@ModelAttribute @Valid OptionRequest optionRequest) {
        optionService.createOption(optionRequest.getProductId(), optionRequest);
        return "redirect:/api/products/" + optionRequest.getProductId() + "/options";
    }

    // 옵션 수정 처리
    @PostMapping("/update/{id}")
    public String updateOption(@PathVariable Long id, @ModelAttribute @Valid OptionRequest optionRequest) {
        optionService.updateOption(id, optionRequest);
        return "redirect:/api/products/" + optionRequest.getProductId() + "/options";
    }

    // 옵션 삭제
    @PostMapping("/delete/{id}")
    public String deleteOption(@PathVariable Long id) {
        Long productId = optionService.deleteOption(id);
        return "redirect:/api/products/" + productId + "/options";
    }
}
