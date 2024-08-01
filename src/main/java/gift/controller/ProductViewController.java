package gift.controller;

import gift.domain.model.dto.ProductResponseDto;
import gift.service.ProductService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/admin")
    public String home(Model model,
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") int page,
        @RequestParam(defaultValue = "10") @Positive int size,
        @RequestParam(defaultValue = "name,asc") String sort,
        @RequestParam Long categoryId) {
        Page<ProductResponseDto> productPage = productService.getAllProducts(page, size, sort, categoryId);
        model.addAttribute("productPage", productPage);
        return "admin";
    }
}