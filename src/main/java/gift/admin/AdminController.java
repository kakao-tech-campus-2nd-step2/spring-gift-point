package gift.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @GetMapping("/admin/products")
    public String admin(Model model) {
        return "admin/index";
    }

    @GetMapping("/admin/products/new")
    public String newProduct(Model model) {
        return "admin/new-product";
    }

    @GetMapping("/admin/edit-product")
    public String editProduct(Model model, @RequestParam("product-id") Long productId) {
        model.addAttribute("productId", productId);
        return "admin/edit-product";
    }
}
