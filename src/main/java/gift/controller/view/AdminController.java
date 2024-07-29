package gift.controller.view;

import gift.entity.Product;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductRepository productRepository;

    public AdminController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping()
    public String adminPage(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin";
    }

    @GetMapping("/add")
    public String getAdminAddPage() {
        return "adminAdd";
    }

    @GetMapping("/deleted")
    public String getAdminDeletedPage() {
        return "adminDeleted";
    }

    @GetMapping("/{id}")
    public String getAdminAddPage(@PathVariable("id") Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) return "error";
        model.addAttribute("product", product);
        return "adminProductDetail";
    }
}
