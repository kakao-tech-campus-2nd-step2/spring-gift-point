package gift.controller.admin;

import gift.DTO.category.CategoryResponse;
import gift.DTO.product.ProductRequest;
import gift.DTO.product.ProductResponse;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final ProductService productService;
    private final CategoryService categoryService;
    public AdminViewController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showProductManagementPage(Model model) {
        try {
            List<ProductResponse> products = productService.getAllProducts();
            if (products == null) {
                model.addAttribute("products", List.of());
                return "productManagement";
            }
            model.addAttribute("products", products);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load products: " + e.getMessage());
        }
        return "productManagement";
    }

    @GetMapping("/product/add")
    public String showProductAddForm(Model model) {
        model.addAttribute("product", new ProductRequest());
        List<CategoryResponse> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "productAddForm";
    }

    @PostMapping("/product/add")
    public String saveProduct(
        @ModelAttribute @Valid ProductRequest newProduct,
        BindingResult bindingResult,
        Model model
     ) {
        productService.addProduct(newProduct);
        return "redirect:/admin";
    }

    @GetMapping("/product/edit/{id}")
    public String showProductEditForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("product", productService.getProductById(id));
            List<CategoryResponse> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
        } catch (RuntimeException e) {
            return "redirect:/admin";
        }

        return "productEditForm";
    }

    @PostMapping("/product/edit/{id}")
    public String updateProduct(
        @PathVariable Long id,
        @ModelAttribute @Valid ProductRequest updateProduct,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            // 에러 메시지 콘솔에 출력
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
            });

            model.addAttribute("product", productService.getProductById(id));
            List<CategoryResponse> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "productEditForm";
        }
        productService.updateProduct(id, updateProduct);
        return "redirect:/admin";
    }

    @GetMapping("product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin";
    }
}
