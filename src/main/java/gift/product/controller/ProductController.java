package gift.product.controller;

import gift.product.model.Product;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;

@Controller
@RequestMapping("/products")
@Validated
// crud를 진행하고 다시 api/products로 보내는 역할
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. product create
    @PostMapping
    public String createProduct(@Valid @ModelAttribute Product product) {
        productService.createProduct(product, product.getCategory().getId());
        return "redirect:/api/products";
    }

    // 2. product update
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute Product product) {
        productService.updateProduct(id, product, product.getCategory().getId());
        return "redirect:/api/products";
    }

    // 3. product delete
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/api/products";
    }

    // product pagination
    @GetMapping("/page")
    @ResponseBody
    public Page<Product> getProductsByPage(@RequestParam int page,
                                           @RequestParam int size,
                                           @RequestParam(defaultValue = "price") String sortBy,
                                           @RequestParam(defaultValue = "desc") String direction) {
        return productService.getProductsByPage(page, size, sortBy, direction);
    }
}
