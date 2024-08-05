package gift.adminController;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.dto.ProductUpdateRequest;
import gift.dto.CategoryUpdateRequest;
import gift.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showProductsPage(Model model) {
        List<ProductResponse> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "productAdmin/admin-products";
    }

    @GetMapping("/create")
    public String showCreateProductPage(Model model) {
        model.addAttribute("product", new ProductRequest());
        return "productAdmin/product-create";
    }

    @PostMapping
    public String createProduct(@Valid @ModelAttribute("product") ProductRequest productRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "productAdmin/product-create";
        }
        productService.createProduct(productRequest, bindingResult);
        return "redirect:/admin/products";
    }

    @GetMapping("/{productId}")
    public String showEditProductPage(@PathVariable("productId") Long productId, Model model) {
        ProductResponse product = productService.getProduct(productId);
        model.addAttribute("product", product);
        return "productAdmin/product-edit";
    }

    @PutMapping("/{productId}")
    public String updateProduct(@PathVariable("productId") Long productId, @Valid @ModelAttribute("product") ProductUpdateRequest productUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "productAdmin/product-edit";
        }
        productService.updateProduct(productId, productUpdateRequest, bindingResult);
        return "redirect:/admin/products";
    }

    @GetMapping("/{productId}/category")
    public String showEditProductCategoryPage(@PathVariable("productId") Long productId, Model model) {
        ProductResponse product = productService.getProduct(productId);
        model.addAttribute("product", product);
        model.addAttribute("categoryUpdateRequest", new CategoryUpdateRequest(product.getCategoryId()));
        return "productAdmin/product-edit-category";
    }

    @PutMapping("/{productId}/category")
    public String updateProductCategory(@PathVariable("productId") Long productId, @Valid @ModelAttribute("categoryUpdateRequest") CategoryUpdateRequest categoryUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "productAdmin/product-edit-category";
        }
        productService.updateProductCategory(productId, categoryUpdateRequest, bindingResult);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return "redirect:/admin/products";
    }
}
