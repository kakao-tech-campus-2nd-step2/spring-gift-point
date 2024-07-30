package gift.product.controller;

import gift.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop/products")
public class KakaoShopController {

    private final ProductService productService;

    @Autowired
    public KakaoShopController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String wishList(Model model, Pageable pageable) {
        System.out.println("[ProductController] showProductList()");
        model.addAttribute("productList", productService.getAllProducts(pageable));
        return "product-list";
    }
}