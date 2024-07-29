package gift.product.controller.product;

import gift.product.dto.product.AdminProductDto;
import gift.product.model.Product;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Hidden
@Controller
@RequestMapping("/admin")
public class AdminProductController {

    private static final String REDIRECT_ADMIN_PRODUCTS = "redirect:/admin/products";
    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = productService.getProductAll();
        model.addAttribute("products", products);
        return "admin/products";
    }

    @GetMapping("/products/insert")
    public String insertForm() {
        return "admin/insertForm";
    }

    @PostMapping("/products/insert")
    public String insertProduct(@Valid AdminProductDto adminProductDto) {
        productService.insertProduct(adminProductDto);
        return REDIRECT_ADMIN_PRODUCTS;
    }

    @GetMapping("/products/update/{id}")
    public String updateForm(@PathVariable(name = "id") Long productId, Model model) {
        Product product = productService.getProduct(productId);
        model.addAttribute("product", product);
        return "admin/updateForm";
    }

    @PutMapping("/products/update/{id}")
    public String updateProduct(@PathVariable(name = "id") Long productId,
        @Valid AdminProductDto adminProductDto) {
        productService.updateProduct(productId, adminProductDto);
        return REDIRECT_ADMIN_PRODUCTS;
    }

    @DeleteMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long productId) {
        productService.deleteProduct(productId);
        return REDIRECT_ADMIN_PRODUCTS;
    }
}
