package gift.controller.web;

import gift.dto.OptionDTO;
import gift.dto.ProductDTO;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/products/{productId}/options")
@Tag(name = "Option Web API", description = "웹 옵션 관련 API")
public class OptionWebController {

    private final OptionService optionService;
    private final ProductService productService;

    @Autowired
    public OptionWebController(OptionService optionService, ProductService productService) {
        this.optionService = optionService;
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "상품 ID로 웹 옵션 조회", description = "지정된 상품 ID에 해당하는 모든 옵션을 웹에서 조회합니다.")
    public String getOptionsByProductId(@PathVariable Long productId, Model model) {
        List<OptionDTO> options = optionService.getOptionsByProductId(productId);
        ProductDTO product = productService.getProductById(productId);
        model.addAttribute("options", options);
        model.addAttribute("product", product);
        return "productOption";
    }
}
