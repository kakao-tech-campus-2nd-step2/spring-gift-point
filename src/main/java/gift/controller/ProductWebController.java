package gift.controller;

import gift.dto.ProductRequestDto;
import gift.model.Product;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/products")
public class ProductWebController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductWebController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllProducts(Model model,
                                 @PageableDefault(size = 10, sort = {"id"},
                                         direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Product> productPage = productService.getWebProducts(pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("pageSize", pageable.getPageSize());
        model.addAttribute("sortField", pageable.getSort().iterator().next().getProperty());
        model.addAttribute("sortDirection", pageable.getSort().iterator().next().getDirection().toString());
        return "index";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "addProduct";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute @Valid ProductRequestDto productRequestDto) {
        productService.addProduct(productRequestDto);
        return "redirect:/web/products";
    }

    @GetMapping("/edit/{productId}")
    public String editProductForm(@PathVariable Long productId, Model model) {
        Product product = productService.getWebProductById(productId);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "editProduct";
    }

    @PostMapping("/edit/{productId}")
    public String updateProduct(@PathVariable Long productId,
                                @ModelAttribute @Valid ProductRequestDto productRequestDto) {
        productService.updateProduct(productId, productRequestDto);
        return "redirect:/web/products";
    }

    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return "redirect:/web/products";
    }
}
