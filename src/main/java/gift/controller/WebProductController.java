package gift.controller;

import gift.dto.ProductRequest;
import gift.entity.Product;
import gift.service.CategoryService;
import gift.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public WebProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    public String viewHomepage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("productsList", products);
        return "index";
    }

    @GetMapping("/showNewProducts")
    public String showNewProducts(Model model) {
        ProductRequest productRequest = new ProductRequest();
        model.addAttribute("product", productRequest);
        model.addAttribute("categories", categoryService.findAll());
        return "newProduct";
    }

    @PostMapping("/saveProducts")
    public String saveProducts(@ModelAttribute("product") ProductRequest productRequest) {
        if (productRequest.getId() == null) {
            productService.addProduct(productRequest);
            return "redirect:/products";
        }
        productService.updateProduct(productRequest.getId(), productRequest);
        return "redirect:/products";
    }

    @GetMapping("/showUpdateProducts/{id}")
    public String showUpdateProducts(@PathVariable(value = "id") Long id, Model model) {
        Optional<Product> product = productService.findById(id);
        if (product.isEmpty()) {
            return "redirect:/products";
        }
        model.addAttribute("product", ProductRequest.from(product.get()));
        model.addAttribute("categories", categoryService.findAll());
        return "updateProduct";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id) {
        if (productService.findById(id).isEmpty()) {
            return "redirect:/products";
        }
        productService.deleteById(id);
        return "redirect:/products";
    }

}
