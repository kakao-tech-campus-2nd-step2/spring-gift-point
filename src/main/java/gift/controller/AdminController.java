package gift.controller;

import gift.domain.ProductDTO;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/admin/products")
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminController(ProductService productService,
                           CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getFrontProduct(Model model) {
        model.addAttribute("products", productService.getAllProduct(0, 10).getContent());
        return "admin";
    }

    @GetMapping("/page")
    public String getProduct(Model model,
                             @ModelAttribute("page") int currentPage,
                             @ModelAttribute("size") int pageSize) {
        model.addAttribute("products", productService.getAllProduct(currentPage, pageSize).getContent());
        return "admin";
    }

    @GetMapping("/add")
    public String addProduct() {
        return "add";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") ProductDTO productDTO) {
        productService.addProduct(productDTO);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/{id}")
    public String delProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "edit";
    }

    @PutMapping( "/{id}")
    public String updateProduct(@PathVariable int id, @Valid @ModelAttribute("product") ProductDTO productDTO) {
        productService.updateProduct(id, productDTO);
        return "redirect:/admin/products";
    }
}