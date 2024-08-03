package gift.web.controller;

import gift.service.category.CategoryService;
import gift.service.product.ProductService;
import gift.web.dto.product.ProductPutRequestDto;
import gift.web.dto.product.ProductRequestDto;
import gift.web.dto.product.ProductResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/products")
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getProducts(Model model, Pageable pageable, @RequestParam(required = false) Long categoryId) {
        model.addAttribute("products", productService.getProducts(categoryId, pageable));
        return "products";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        ProductResponseDto productResponseDto = productService.getProductById(id);
        model.addAttribute("product", productResponseDto);
        return "products";
    }

    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new ProductRequestDto( "name", 0L, "image.url", null, null));
        model.addAttribute("categories", categoryService.getCategories());
        return "create";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute @Valid ProductRequestDto productRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getCategories());
            return "create";
        }
        productService.createProduct(productRequestDto);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        ProductResponseDto productResponseDto = productService.getProductById(id);
        model.addAttribute("product", productResponseDto);
        model.addAttribute("categories", categoryService.getCategories());
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute @Valid ProductPutRequestDto productputRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productputRequestDto);
            model.addAttribute("categories", categoryService.getCategories());
            model.addAttribute("org.springframework.validation.BindingResult.product", bindingResult);
            return "edit";
        }
        productService.updateProduct(id, productputRequestDto);
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
