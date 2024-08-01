package gift.view.controller;

import gift.config.PageConfig;
import gift.product.category.dto.response.CategoryResponse;
import gift.product.category.service.CategoryService;
import gift.product.dto.response.ProductResponse;
import gift.product.entity.Product;
import gift.product.service.ProductService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductViewController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllProducts(
        @PageableDefault(
            size = PageConfig.PAGE_PER_COUNT,
            sort = PageConfig.SORT_STANDARD,
            direction = Direction.DESC
        ) Pageable pageable,
        Model model
    ) {
        Page<ProductResponse> products = productService.getAllProducts(pageable);

        model.addAttribute("productPage", products);
        model.addAttribute("maxPage", 10);
        return "product_list";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product(null, null, null, null));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product_add_form";
    }

    @GetMapping("edit/{id}")
    public String editProductPage(Model model, @PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        List<CategoryResponse> categories = categoryService.getAllCategories();

        System.out.println("Product: " + product);
        System.out.println("Categories: " + categories);

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);

        return "product_edit_form";
    }

}
