package gift.product.controller;

import gift.category.model.Category;
import gift.category.service.CategoryService;
import gift.common.exception.ProductNotFoundException;
import gift.option.domain.Option;
import gift.option.service.OptionService;
import gift.product.model.Product;
import gift.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/products")
public class ShowProductPageController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public ShowProductPageController(ProductService productService, CategoryService categoryService, OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    // 상품 메인 페이지 반환
    @GetMapping
    public String showProductForm(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "Product/product";
    }

    // 상품 등록 페이지 반환
    @GetMapping("/new")
    public String showProductCreateForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        return "Product/create_product";
    }

    // 상품 수정 페이지 반환
    @GetMapping("/edit/{id}")
    public String showProductEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", product);
        return "Product/edit_product";
    }
}
