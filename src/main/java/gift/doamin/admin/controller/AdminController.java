package gift.doamin.admin.controller;

import gift.doamin.product.dto.ProductResponse;
import gift.doamin.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showAdminPage() {
        return "admin/index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "admin/login";
    }

    @GetMapping("/register")
    public String showSignUpPage() {
        return "admin/signup";
    }

    @GetMapping("/category")
    public String showCategoryPage() {
        return "admin/category";
    }

    @GetMapping("/product")
    public String showProductPage() {
        return "admin/product";
    }
}