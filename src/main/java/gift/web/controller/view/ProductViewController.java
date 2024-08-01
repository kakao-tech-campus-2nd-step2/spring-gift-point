package gift.web.controller.view;

import gift.authentication.annotation.LoginMember;
import gift.config.KakaoProperties;
import gift.service.ProductService;
import gift.web.dto.MemberDetails;
import gift.web.dto.form.CreateProductForm;
import gift.web.dto.response.product.ReadAllProductsResponse;
import gift.web.dto.response.product.ReadProductResponse;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class ProductViewController {

    private final ProductService productService;

    private final KakaoProperties kakaoProperties;

    public ProductViewController(ProductService productService, KakaoProperties kakaoProperties) {
        this.productService = productService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/products")
    public String readAdminPage(Model model) {
        ReadAllProductsResponse allProductsResponse = productService.readAllProducts();
        List<ReadProductResponse> products = allProductsResponse.getProducts();
        model.addAttribute("products", products);
        return "admin";
    }

    @GetMapping("/products/add")
    public String addForm(Model model) {
        model.addAttribute("product", new CreateProductForm());
        return "form/add-product-form";
    }

    @GetMapping("/products/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        ReadProductResponse product = productService.readProductById(id);
        model.addAttribute("product", product);
        return "form/edit-product-form";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("clientId", kakaoProperties.getClientId());
        model.addAttribute("redirectUri", kakaoProperties.getRedirectUri());
        return "form/login-form";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "form/register-form";
    }

    @GetMapping("/login-callback")
    public String loginCallback(@LoginMember MemberDetails memberDetails, Model model) {
        model.addAttribute("member", memberDetails);
        return "login-callback";
    }
}
