package gift.admin.controller;

import gift.category.service.CategoryService;
import gift.product.service.OptionService;
import gift.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final OptionService optionService;
    private final CategoryService categoryService;

    @Autowired
    public AdminProductController(ProductService productService, CategoryService categoryService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getProducts(Model model, Pageable pageable) {
        model.addAttribute("pages", productService.getProducts(null, pageable));
        return "products";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProduct(null, id));
        model.addAttribute("options", optionService.getOptions(id));
        return "product";
    }

    @GetMapping("/new")
    public String addProduct(Model model) {
        model.addAttribute("categories", categoryService.getCategories());
        return "new-product";
    }

    @GetMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProduct(null, id));
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("options", optionService.getOptions(id));
        return "edit-product";
    }
}
