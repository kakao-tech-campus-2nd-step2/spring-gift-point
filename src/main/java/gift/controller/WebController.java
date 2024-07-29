package gift.controller;

import gift.model.Category;
import gift.model.Product;
import gift.model.ProductOption;
import gift.service.CategoryService;
import gift.service.ProductOptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    @Autowired
    private ProductController productController;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductOptionService productOptionService;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @GetMapping("/order")
    public String showOrderForm(@RequestParam(name = "optionId", required = false) Long optionId, Model model) {
        model.addAttribute("optionId", optionId);
        return "order";
    }

    // @GetMapping("/login")
    // public String showLoginForm() {
    //    return "login";
    // }

    @GetMapping("/user-wishes")
    public String showWishesPage() {
        return "user-wishes";
    }

    @GetMapping("/user-products")
    public String showUserProductsPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        ResponseEntity<Page<Product>> response = productController.getAllProducts(pageable);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            return "error/500";
        }
        model.addAttribute("products", response.getBody());
        return "user-products";
    }

    @GetMapping("/products")
    public String viewProductPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        ResponseEntity<Page<Product>> response = productController.getAllProducts(pageable);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            return "error/500";
        }
        Page<Product> productPage = response.getBody();
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("pageSize", size);
        return "product/index";
    }

    @GetMapping("/products/new")
    public String showNewProductForm(Model model) {
        Product product = new Product();
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "product/new";
    }

    @PostMapping("/products")
    public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    model.addAttribute("valid_" + error.getField(), error.getDefaultMessage())
            );
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "product/new";
        }
        setProductOptions(product);
        ResponseEntity<Object> response = productController.addProduct(product, bindingResult);
        if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            Map<String, Object> errors = (Map<String, Object>) response.getBody();
            errors.forEach((key, value) -> model.addAttribute("valid_" + key, value.toString()));
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "product/new";
        }
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        ResponseEntity<Product> response = productController.getProductById(id);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            return "error/500";
        }
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("product", response.getBody());
        model.addAttribute("categories", categories);
        return "product/edit";
    }

    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    model.addAttribute("valid_" + error.getField(), error.getDefaultMessage())
            );
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "product/edit";
        }
        setProductOptions(product);
        ResponseEntity<Object> response = productController.updateProduct(id, product, bindingResult);
        if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            Map<String, Object> errors = (Map<String, Object>) response.getBody();
            errors.forEach((key, value) -> model.addAttribute("valid_" + key, value.toString()));
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "product/edit";
        }
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        ResponseEntity<Void> response = productController.deleteProduct(id);
        if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
            return "error/500";
        }
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/options")
    public String viewProductOptions(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "error/404";
        }
        model.addAttribute("product", product);
        model.addAttribute("options", product.getOptions());
        return "product/options";
    }

    private void setProductOptions(Product product) {
        if (product.getOptions() != null) {
            for (ProductOption option : product.getOptions()) {
                option.setProduct(product);
            }
        }
    }
}