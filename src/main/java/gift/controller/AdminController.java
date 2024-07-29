package gift.controller;

import gift.dto.ProductDTO;
import gift.entity.Category;
import gift.entity.Product;
import gift.service.ProductFacadeService;
import gift.service.ProductService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    ProductFacadeService productService;

    public AdminController(ProductFacadeService productService) {
        this.productService = productService;
    }

    //기본화면
    @GetMapping
    public String admin() {
        return "admin";

    }

    //상품 관리 화면
    @GetMapping("/product-management")
    public String productManage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        List<Category> categories = productService.findAllCategory();
        model.addAttribute("categories", categories);
        return "product-manage";

    }


    //상품 추가 화면
    @GetMapping("/product-add")
    public String productAdd(Model model) {
        List<Category> categories = productService.findAllCategory();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        return "product-add";
    }

    @PostMapping("product/submit")
    public String submitProductForm(@ModelAttribute Product product, Model model) {
        model.addAttribute("product", product);
        return "product-manage";
    }

}
