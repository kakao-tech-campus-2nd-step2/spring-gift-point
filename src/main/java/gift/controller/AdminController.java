package gift.controller;

import gift.dto.ProductDTO;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 페이지", description = "관리자 페이지를 리다이렉트 함")
@Controller
@RequestMapping("/admin/products")
public class AdminController {
    private final ProductService productService;

    @Autowired
    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("productDTO", productService.getAllProducts());
        return "list";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new ProductDTO()); // 빈 Product 객체를 생성하여 모델에 추가
        return "create";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute @Valid ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create";
        }

        productService.createProduct(productDTO);
        return "redirect:/admin/products";
    }

    @GetMapping("/update/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        var productDTO = productService.getProduct(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("productDTO", productDTO);
        return "update";
    }

    @PostMapping("/update/{id}")
    public String editProduct(@PathVariable Long id, @Valid @ModelAttribute ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "update";
        }

        productService.editProduct(id, productDTO);
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
