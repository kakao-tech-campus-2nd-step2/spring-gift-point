package gift.controller.productlist;

import gift.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductListViewController {

    private final ProductService productService;

    @Autowired
    public ProductListViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "productList";
    }
}